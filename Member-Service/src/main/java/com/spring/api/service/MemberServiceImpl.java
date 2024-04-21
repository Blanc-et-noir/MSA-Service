package com.spring.api.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.api.code.ErrorCode;
import com.spring.api.dto.CreateMemberRequestDTO;
import com.spring.api.dto.MemberDTO;
import com.spring.api.dto.SimpleMemberDTO;
import com.spring.api.dto.UpdateMemberPWRequestDTO;
import com.spring.api.dto.UpdateMemberTokensRequestDTO;
import com.spring.api.entity.MemberEntity;
import com.spring.api.enumeration.MemberRole;
import com.spring.api.enumeration.MemberStatus;
import com.spring.api.exception.CustomException;
import com.spring.api.repository.MemberRepository;
import com.spring.api.util.RedisUtil;

import jakarta.transaction.Transactional;

@Transactional
@Service("memberService")
public class MemberServiceImpl implements MemberService{
	private RedisUtil redisUtil;
	private MemberRepository memberRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private final String PREFIX_FOR_VERIFICATION_CODE_WITH_MEMBER_EMAIL = "verification-code-with-member-email-for-";
	private final String PREFIX_FOR_VERIFICATION_WITH_MEMBER_EMAIL = "verification-with-member-email-for-";
	private final String PREFIX_FOR_VERIFICATION_CODE_WITH_MEMBER_PW = "verification-code-with-member-pw-for-";
	private final String PREFIX_FOR_VERIFICATION_WITH_MEMBER_PW = "verification-with-member-pw-for-";
	
	MemberServiceImpl(RedisUtil redisUtil,MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
		this.redisUtil = redisUtil;
		this.memberRepository = memberRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@Override
	public void createMember(CreateMemberRequestDTO dto) throws RuntimeException {
		Optional<MemberEntity> oldMember = memberRepository.findById(dto.getMemberID());
		
		if(oldMember.isPresent()) {
			throw new CustomException(ErrorCode.MEMBER_ID_ALREADY_OCCUPIED);
		}
		
		if(!dto.getMemberPW().equals(dto.getMemberPWCheck())) {
			throw new CustomException(ErrorCode.MEMBER_PW_NOT_MATCHED_TO_EACH_OTHER);
		}
		
		if(redisUtil.getString(PREFIX_FOR_VERIFICATION_WITH_MEMBER_EMAIL+dto.getMemberEmail())==null) {
			throw new CustomException(ErrorCode.MEMBER_EMAIL_NOT_VERIFIED);
		}
		
		if(memberRepository.findByMemberEmail(dto.getMemberEmail()).isPresent()) {
			throw new CustomException(ErrorCode.MEMBER_EMAIL_ALREADY_OCCUPIED);
		}
		
		MemberEntity member = MemberEntity.builder()
				.memberID(dto.getMemberID())
				.memberPW(bCryptPasswordEncoder.encode(dto.getMemberPW()))
				.memberRole(MemberRole.GENERAL)
				.memberName(dto.getMemberName())
				.memberEmail(dto.getMemberEmail())
				.memberStatus(MemberStatus.NORMAL)
				.memberJoinTime(LocalDateTime.now())
				.build();
		
		memberRepository.save(member);
		
		redisUtil.delete(PREFIX_FOR_VERIFICATION_WITH_MEMBER_EMAIL+dto.getMemberEmail());
	}

	@Override
	public void updateMemberTokens(String memberId, UpdateMemberTokensRequestDTO dto) {
		Optional<MemberEntity> member = memberRepository.findById(memberId);
		
		if(!member.isPresent()||!member.get().isMemberStatusNormal()) {
			throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
		}
		
		member.get().updateTokens(dto.getMemberAccessToken(), dto.getMemberRefreshToken());
	}

	@Override
	public MemberDTO readMemberByMemberID(String memberId) {
		Optional<MemberEntity> member = memberRepository.findById(memberId);
		
		if(!member.isPresent()||!member.get().isMemberStatusNormal()) {
			throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
		}
		
		return MemberDTO.builder()
				.memberID(memberId)
				.memberRole(member.get().getMemberRole())
				.memberPW(member.get().getMemberPW())
				.memberEmail(member.get().getMemberEmail())
				.memberAccessToken(member.get().getMemberAccessToken())
				.memberRefreshToken(member.get().getMemberRefreshToken())
				.build();
	}

	@Override
	public MemberDTO updateMemberPW(String memberId, UpdateMemberPWRequestDTO dto) {
		Optional<MemberEntity> member = memberRepository.findById(memberId);
		
		if(!member.isPresent()||!member.get().isMemberStatusNormal()) {
			throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
		}
		
		member.get().updatePw(bCryptPasswordEncoder.encode(dto.getTemporaryMemberPW()));
		
		return MemberDTO.builder()
				.memberID(member.get().getMemberID())
				.memberPW(member.get().getMemberPW())
				.memberEmail(member.get().getMemberEmail())
				.memberRole(member.get().getMemberRole())
				.build();
	}

	@Override
	public SimpleMemberDTO readMemberByMemberEmail(String memberEmail) {
		Optional<MemberEntity> member = memberRepository.findByMemberEmail(memberEmail);
		
		if(!member.isPresent()||!member.get().isMemberStatusNormal()) {
			throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
		}
		
		return SimpleMemberDTO.builder()
				.memberID(member.get().getMemberID())
				.memberEmail(member.get().getMemberEmail())
				.build();
	}

}
