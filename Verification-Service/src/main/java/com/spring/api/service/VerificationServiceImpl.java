package com.spring.api.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.spring.api.code.MemberServiceCode;
import com.spring.api.dto.CreateVerificationCodeForMemberEmailRequestDTO;
import com.spring.api.dto.CreateVerificationCodeForMemberPWRequestDTO;
import com.spring.api.dto.DeleteVerificationCodeForMemberEmailRequestDTO;
import com.spring.api.dto.DeleteVerificationCodeForMemberPWRequestDTO;
import com.spring.api.dto.MemberDTO;
import com.spring.api.dto.ReadMemberResponseDTO;
import com.spring.api.dto.UpdateMemberPWRequestDTO;
import com.spring.api.dto.UpdateMemberPWResponseDTO;
import com.spring.api.exception.CustomException;
import com.spring.api.util.RedisUtil;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service("verificationService")
public class VerificationServiceImpl implements VerificationService{
	private JavaMailSender javaMailSender;
	private RedisUtil redisUtil;
	private RestTemplate restTemplate;
	private final long VERIFICATION_DURATION;
	private final long VERIFICATION_CODE_DURATION;
	private final long VERIFICATION_CODE_LENGTH;
	private final long TEMPORARY_MEMBER_PW_LENGTH;
	private final String MEMBER_SERVICE_BASE_URI = "http://localhost:3002/api/v1";
	private final String PREFIX_FOR_VERIFICATION_CODE_WITH_MEMBER_EMAIL = "verification-code-with-member-email-for-";
	private final String PREFIX_FOR_VERIFICATION_WITH_MEMBER_EMAIL = "verification-with-member-email-for-";
	private final String PREFIX_FOR_VERIFICATION_CODE_WITH_MEMBER_PW = "verification-code-with-member-pw-for-";
	private final String PREFIX_FOR_VERIFICATION_WITH_MEMBER_PW = "verification-with-member-pw-for-";
	
	VerificationServiceImpl(
			JavaMailSender javaMailSender,
			RedisUtil redisUtil,
			RestTemplate restTemplate,
			@Value("${verification.duration}") int VERIFICATION_DURATION,
			@Value("${verification.code.duration}") int VERIFICATION_CODE_DURATION,
			@Value("${verification.code.length}") int VERIFICATION_CODE_LENGTH,
			@Value("${temporary.member.pw.length}") int TEMPORARY_MEMBER_PW_LENGTH
			){
		this.javaMailSender = javaMailSender;
		this.redisUtil = redisUtil;
		this.restTemplate = restTemplate;
		this.VERIFICATION_DURATION = VERIFICATION_DURATION;
		this.VERIFICATION_CODE_LENGTH = VERIFICATION_CODE_LENGTH;
		this.VERIFICATION_CODE_DURATION = VERIFICATION_CODE_DURATION;
		this.TEMPORARY_MEMBER_PW_LENGTH = TEMPORARY_MEMBER_PW_LENGTH;
	}
	
	@Override
	public void createVerificationCodeForMemberEmail(CreateVerificationCodeForMemberEmailRequestDTO dto) {
		final String VERIFICATION_CODE = createVerificationCode(VERIFICATION_CODE_LENGTH);
		
		//이메일 중복 확인
		MemberDTO member = requestMemberByMemberEmail(dto.getMemberEmail());
		
		if(member!=null) {
			throw new CustomException(MemberServiceCode.MEMBER_EMAIL_ALREADY_OCCUPIED);
		}
		
		sendVerificationCodeForMemberEmail(dto.getMemberEmail(),VERIFICATION_CODE);
	}
	
	@Override
	public void createVerificationCodeForMemberPW(CreateVerificationCodeForMemberPWRequestDTO dto) {
		final String VERIFICATION_CODE = createVerificationCode(VERIFICATION_CODE_LENGTH);
		
		//ID와 이메일 연동여부 확인
		MemberDTO member = requestMemberByMemberID(dto.getMemberID());
		
		if(member==null) {
			throw new CustomException(MemberServiceCode.MEMBER_NOT_FOUND);
		}
		
		if(!dto.getMemberEmail().equals(member.getMemberEmail())) {
			throw new CustomException(MemberServiceCode.MEMBER_EMAIL_NOT_BOUND_TO_MEMBER_ID);
		}
		
		sendVerificationCodeForMemberPW(dto.getMemberEmail(),VERIFICATION_CODE);
	}
	
	@Override
	public void deleteVerificationCodeForMemberEmail(DeleteVerificationCodeForMemberEmailRequestDTO dto) {
		//이메일에 대한 인증코드 얻기
		final String VERIFICATION_CODE = redisUtil.getString(PREFIX_FOR_VERIFICATION_CODE_WITH_MEMBER_EMAIL+dto.getMemberEmail());
		
		if(VERIFICATION_CODE==null) {
			throw new CustomException(MemberServiceCode.MEMBER_EMAIL_VERIFICATION_CODE_NOT_FOUND);
		}
		
		//인증코드 비교
		if(!dto.getMemberEmailVerificationCode().equals(VERIFICATION_CODE)) {
			throw new CustomException(MemberServiceCode.MEMBER_EMAIL_VERIFICATION_CODE_IS_WRONG);
		}
		
		//인증완료시, 인증코드 삭제 및 이메일 30분간 인증처리
		redisUtil.delete(PREFIX_FOR_VERIFICATION_CODE_WITH_MEMBER_EMAIL+dto.getMemberEmail());
		redisUtil.setString(PREFIX_FOR_VERIFICATION_WITH_MEMBER_EMAIL+dto.getMemberEmail(), "verified", VERIFICATION_DURATION*1000);
	}

	@Override
	public void deleteVerificationCodeForMemberPW(DeleteVerificationCodeForMemberPWRequestDTO dto) {
		//이메일에 대한 인증코드 얻기
		final String VERIFICATION_CODE = redisUtil.getString(PREFIX_FOR_VERIFICATION_CODE_WITH_MEMBER_PW+dto.getMemberEmail());
				
		if(VERIFICATION_CODE==null) {
			throw new CustomException(MemberServiceCode.MEMBER_EMAIL_VERIFICATION_CODE_NOT_FOUND);
		}
				
		//인증코드 비교
		if(!dto.getMemberEmailVerificationCode().equals(VERIFICATION_CODE)) {
			throw new CustomException(MemberServiceCode.MEMBER_EMAIL_VERIFICATION_CODE_IS_WRONG);
		}
		
		//ID와 이메일 연동여부 확인
		MemberDTO member = requestMemberByMemberID(dto.getMemberID());
		if(!dto.getMemberEmail().equals(member.getMemberEmail())) {
			throw new CustomException(MemberServiceCode.MEMBER_EMAIL_NOT_BOUND_TO_MEMBER_ID);
		}
		
		final String TEMPORARY_MEMBER_PW = createTemporaryMemberPW(TEMPORARY_MEMBER_PW_LENGTH);
		
		//비밀번호 변경 요청
		updateMemberPW(dto.getMemberID(), UpdateMemberPWRequestDTO.builder()
			.temporaryMemberPW(TEMPORARY_MEMBER_PW)
			.build()
		);
		
		sendTemporaryMemberPW(dto.getMemberID(), dto.getMemberEmail(), TEMPORARY_MEMBER_PW);
		
		//인증완료시, 인증코드 삭제 및 이메일 30분간 인증처리
		redisUtil.delete(PREFIX_FOR_VERIFICATION_CODE_WITH_MEMBER_PW+dto.getMemberEmail());
	}
	
	@Async
	private void sendVerificationCodeForMemberEmail(String memberEmail, String VERIFICATION_CODE) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;
		try {
			mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
	        mimeMessageHelper.setTo(memberEmail);
	        mimeMessageHelper.setSubject("이메일 인증코드");
	        mimeMessageHelper.setText(memberEmail+"에 대한 인증코드는 "+VERIFICATION_CODE+" 입니다.\n해당 인증코드는 "+VERIFICATION_CODE_DURATION+"초 동안 유효합니다.", false);
	        javaMailSender.send(mimeMessage);
	        redisUtil.setString(PREFIX_FOR_VERIFICATION_CODE_WITH_MEMBER_EMAIL+memberEmail, VERIFICATION_CODE, VERIFICATION_CODE_DURATION*1000);
		} catch (MessagingException e) {
			throw new CustomException(MemberServiceCode.MEMBER_EMAIL_VERIFICATION_CODE_NOT_SENT);
		}
	}
	
	@Async
	private void sendVerificationCodeForMemberPW(String memberEmail, String VERIFICATION_CODE) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;
		try {
			mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
	        mimeMessageHelper.setTo(memberEmail);
	        mimeMessageHelper.setSubject("이메일 인증코드");
	        mimeMessageHelper.setText(memberEmail+"에 대한 인증코드는 "+VERIFICATION_CODE+" 입니다.\n해당 인증코드는 "+VERIFICATION_CODE_DURATION+"초 동안 유효합니다.", false);
	        javaMailSender.send(mimeMessage);
	        redisUtil.setString(PREFIX_FOR_VERIFICATION_CODE_WITH_MEMBER_PW+memberEmail, VERIFICATION_CODE, VERIFICATION_CODE_DURATION*1000);
		} catch (MessagingException e) {
			throw new CustomException(MemberServiceCode.MEMBER_EMAIL_VERIFICATION_CODE_NOT_SENT);
		}
	}
	
	@Async
	private void sendTemporaryMemberPW(String memberID, String memberEmail, String TEMPORARY_MEMBER_PW) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;
		try {
			mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
	        mimeMessageHelper.setTo(memberEmail);
	        mimeMessageHelper.setSubject("임시 PW");
	        mimeMessageHelper.setText(memberID+"에 대한 임시 PW는 "+TEMPORARY_MEMBER_PW+" 입니다.\n임시 PW를 사용하여 로그인후, 반드시 PW를 변경해주시기 바랍니다.", false);
	        javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new CustomException(MemberServiceCode.MEMBER_EMAIL_VERIFICATION_CODE_NOT_SENT);
		}
	}
	
	private String createVerificationCode(long length) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		
		for(int i=0;i<length;i++) {
			sb.append(random.nextLong(length));
		}
		
		return sb.toString();
	}
	
	private String createTemporaryMemberPW(long length) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		final String[] array = {"`~!@$!%*#^?&","0123456789","abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"};
		
		for(int i=0; i<length; i++) {
			int j = random.nextInt(3);
			int k = random.nextInt(array[j].length());
			
			sb.append(array[j].charAt(k));
		}
	    
		sb.append(array[2].charAt(array[2].length()-1));
		sb.append(array[1].charAt(array[1].length()-1));
		sb.append(array[0].charAt(array[0].length()-1));
		
	    return sb.toString();
	}

	private MemberDTO requestMemberByMemberID(String memberID) {		
		try {
			return  restTemplate.getForObject(MEMBER_SERVICE_BASE_URI+"/members/"+memberID+"?member-statuses=NORMAL", ReadMemberResponseDTO.class).getData();
		}catch(Exception e) {
			return null;
		}
	}
	
	private MemberDTO requestMemberByMemberEmail(String memberEmail) {
		try {
			return restTemplate.getForObject(MEMBER_SERVICE_BASE_URI+"/members/member-emails/"+memberEmail, ReadMemberResponseDTO.class).getData();
		}catch(Exception e) {
			return null;
		}
	}
	
	private void updateMemberPW(String memberID, UpdateMemberPWRequestDTO dto) {
		restTemplate.patchForObject(MEMBER_SERVICE_BASE_URI+"/members/"+memberID+"/member-pws",dto,UpdateMemberPWResponseDTO.class).getData();
	}
	
}
