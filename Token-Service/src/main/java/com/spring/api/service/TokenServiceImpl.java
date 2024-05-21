package com.spring.api.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.IncorrectClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.MissingClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.spring.api.code.DefaultServiceCode;
import com.spring.api.code.MemberServiceCode;
import com.spring.api.code.TokenServiceCode;
import com.spring.api.dto.CreateMemberTokensRequestDTO;
import com.spring.api.dto.CreateMemberTokensResponseDTO;
import com.spring.api.dto.DeleteMemberTokensRequestDTO;
import com.spring.api.dto.MemberDTO;
import com.spring.api.dto.ReadMemberResponseDTO;
import com.spring.api.dto.UpdateMemberTokensRequestDTO;
import com.spring.api.dto.UpdateMemberTokensResponseDTO;
import com.spring.api.enumeration.MemberRole;
import com.spring.api.exception.CustomException;
import com.spring.api.util.RedisUtil;
import com.spring.api.util.TokenUtil;

import jakarta.transaction.Transactional;

@Transactional
@Service("tokenService")
public class TokenServiceImpl implements TokenService{
	private final String MEMBER_SERVICE_BASE_URI = "http://localhost:3002/api/v1";
	private RedisUtil redisUtil;
	private RestTemplate restTemplate;
	private TokenUtil tokenUtil;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	TokenServiceImpl(RedisUtil redisUtil, TokenUtil tokenUtil, RestTemplate restTemplate, BCryptPasswordEncoder bCryptPasswordEncoder){
		this.redisUtil = redisUtil;
		this.restTemplate = restTemplate;
		this.tokenUtil = tokenUtil;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@Override
	public CreateMemberTokensResponseDTO createTokens(CreateMemberTokensRequestDTO dto){
		//멤버 서비스 조회
		String memberID = dto.getMemberID();
		MemberDTO other = requestMemberByMemberID(memberID);
		
		if(other==null) {
			throw new CustomException(MemberServiceCode.MEMBER_NOT_FOUND);
		}
		
		//정보 비교
		if(!bCryptPasswordEncoder.matches(dto.getMemberPW(), other.getMemberPW())) {
			throw new CustomException(MemberServiceCode.MEMBER_NOT_FOUND);
		}
		
		//토큰 생성
		final String newMemberAccessToken = tokenUtil.createMemberAccessToken(memberID, other.getMemberRole());
		final String newMemberRefreshToken = tokenUtil.createMemberRefreshToken(memberID, other.getMemberRole());
		
		//토큰 정보 변경
		requestUpdateMemberTokens(memberID,UpdateMemberTokensRequestDTO.builder()
			.memberAccessToken(newMemberAccessToken)
			.memberRefreshToken(newMemberRefreshToken)
			.build()
		);
		
		//기존 토큰 폐지
		String oldMemberAccessToken = other.getMemberAccessToken();
		String oldMemberRefreshToken = other.getMemberRefreshToken();
		
		redisUtil.setString(oldMemberAccessToken, "invalidated", tokenUtil.getRemainingTimeWithDecoding(oldMemberAccessToken));
		redisUtil.setString(oldMemberRefreshToken, "invalidated", tokenUtil.getRemainingTimeWithDecoding(oldMemberRefreshToken));
		
		return CreateMemberTokensResponseDTO.builder()
				.memberAccessToken(newMemberAccessToken)
				.memberRefreshToken(newMemberRefreshToken)
				.build();
	}
	
	@Override
	public UpdateMemberTokensResponseDTO updateTokens(UpdateMemberTokensRequestDTO dto) {
		String oldMemberAccessToken = dto.getMemberAccessToken();
		String oldMemberRefreshToken = dto.getMemberRefreshToken();
		
		try {			
			if(oldMemberAccessToken==null) {
				throw new CustomException(TokenServiceCode.TOKEN_FOR_ACCESS_NOT_FOUND);
			}
			
			if(!tokenUtil.isAccessTokenWithDecoding(oldMemberAccessToken)) {
				throw new CustomException(TokenServiceCode.TOKEN_NOT_FOR_ACCESS);
			}
			
			if(redisUtil.getString(oldMemberAccessToken)!=null) {
				throw new CustomException(TokenServiceCode.TOKEN_FOR_ACCESS_INVALIDATED);
			}
			
			tokenUtil.verify(oldMemberAccessToken);

		}catch(CustomException e) {
			throw e;
		}catch(AlgorithmMismatchException e) {
			throw new CustomException(TokenServiceCode.TOKEN_FOR_ACCESS_FORGED);
		}catch(SignatureVerificationException e) {
			throw new CustomException(TokenServiceCode.TOKEN_FOR_ACCESS_FORGED);
		}catch(TokenExpiredException e) {
			
		}catch(MissingClaimException e) {
			throw new CustomException(TokenServiceCode.TOKEN_FOR_ACCESS_FORGED);
		}catch(IncorrectClaimException e) {
			throw new CustomException(TokenServiceCode.TOKEN_FOR_ACCESS_FORGED);
		}catch(JWTVerificationException e) {
			throw new CustomException(TokenServiceCode.TOKEN_FOR_ACCESS_FORGED);
		}catch(Exception e) {
			throw new CustomException(DefaultServiceCode.INTERNAL_SERVER_ERROR);
		}
		
		try {
			if(oldMemberRefreshToken==null) {
				throw new CustomException(TokenServiceCode.TOKEN_FOR_REFRESH_NOT_FOUND);
			}
			
			if(tokenUtil.isExpiredWithDecoding(oldMemberRefreshToken)) {
				throw new CustomException(TokenServiceCode.TOKEN_FOR_REFRESH_EXPIRED);
			}
			
			if(!tokenUtil.isRefreshTokenWithDecoding(oldMemberRefreshToken)) {
				throw new CustomException(TokenServiceCode.TOKEN_NOT_FOR_REFRESH);
			}
			
			if(redisUtil.getString(oldMemberRefreshToken)!=null) {
				throw new CustomException(TokenServiceCode.TOKEN_FOR_REFRESH_INVALIDATED);
			}
			
			tokenUtil.verify(oldMemberRefreshToken);
			
		}catch(CustomException e) {
			throw e;
		}catch(AlgorithmMismatchException e) {
			throw new CustomException(TokenServiceCode.TOKEN_FOR_REFRESH_FORGED);
		}catch(SignatureVerificationException e) {
			throw new CustomException(TokenServiceCode.TOKEN_FOR_REFRESH_FORGED);
		}catch(TokenExpiredException e) {
			throw new CustomException(TokenServiceCode.TOKEN_FOR_REFRESH_EXPIRED);
		}catch(MissingClaimException e) {
			throw new CustomException(TokenServiceCode.TOKEN_FOR_REFRESH_FORGED);
		}catch(IncorrectClaimException e) {
			throw new CustomException(TokenServiceCode.TOKEN_FOR_REFRESH_FORGED);
		}catch(JWTVerificationException e) {
			throw new CustomException(TokenServiceCode.TOKEN_FOR_REFRESH_FORGED);
		}catch(Exception e) {
			throw new CustomException(DefaultServiceCode.INTERNAL_SERVER_ERROR);
		}
		
		if(!tokenUtil.getMemberIDWithDecoding(oldMemberAccessToken).equals(tokenUtil.getMemberIDWithDecoding(oldMemberRefreshToken))) {
			throw new CustomException(TokenServiceCode.TOKEN_OWNER_NOT_MATCHED_TO_EACH_OTHER);
		}
		
		String memberID = tokenUtil.getMemberIDWithDecoding(oldMemberAccessToken);
		String memberRole = tokenUtil.getMemberRoleWithDecoding(oldMemberRefreshToken);
		
		MemberDTO member = requestMemberByMemberID(memberID);
		
		if(member==null) {
			throw new CustomException(MemberServiceCode.MEMBER_NOT_FOUND);
		}
		
		String newMemberAccessToken = tokenUtil.createMemberAccessToken(memberID, MemberRole.from(memberRole));
		String newMemberRefreshToken = tokenUtil.createMemberRefreshToken(memberID, MemberRole.from(memberRole));
		
		requestUpdateMemberTokens(memberID,
				UpdateMemberTokensRequestDTO
				.builder()
				.memberAccessToken(newMemberAccessToken)
				.memberRefreshToken(newMemberRefreshToken)
				.build());
		
		redisUtil.setString(oldMemberAccessToken,"invalidated", tokenUtil.getRemainingTimeWithDecoding(oldMemberAccessToken));
		redisUtil.setString(oldMemberRefreshToken,"invalidated", tokenUtil.getRemainingTimeWithDecoding(oldMemberRefreshToken));
		
		return UpdateMemberTokensResponseDTO.builder()
				.memberAccessToken(newMemberAccessToken)
				.memberRefreshToken(newMemberRefreshToken)
				.build();
	}

	@Override
	public void deleteTokens(DeleteMemberTokensRequestDTO dto) {
		String oldMemberAccessToken = dto.getMemberAccessToken();
		String oldMemberRefreshToken = dto.getMemberRefreshToken();
		
		try {
			if(oldMemberAccessToken==null) {
				throw new CustomException(TokenServiceCode.TOKEN_FOR_ACCESS_NOT_FOUND);
			}
			
			if(tokenUtil.isExpiredWithDecoding(oldMemberAccessToken)) {
				throw new CustomException(TokenServiceCode.TOKEN_FOR_ACCESS_EXPIRED);
			}
			
			if(!tokenUtil.isAccessTokenWithDecoding(oldMemberAccessToken)) {
				throw new CustomException(TokenServiceCode.TOKEN_NOT_FOR_ACCESS);
			}
			
			if(redisUtil.getString(oldMemberAccessToken)!=null) {
				throw new CustomException(TokenServiceCode.TOKEN_FOR_ACCESS_INVALIDATED);
			}
			
			tokenUtil.verify(oldMemberAccessToken);
		}catch(CustomException e) {
			throw e;
		}catch(AlgorithmMismatchException e) {
			throw new CustomException(TokenServiceCode.TOKEN_FOR_ACCESS_FORGED);
		}catch(SignatureVerificationException e) {
			throw new CustomException(TokenServiceCode.TOKEN_FOR_ACCESS_FORGED);
		}catch(TokenExpiredException e) {
			throw new CustomException(TokenServiceCode.TOKEN_FOR_ACCESS_EXPIRED);
		}catch(MissingClaimException e) {
			throw new CustomException(TokenServiceCode.TOKEN_FOR_ACCESS_FORGED);
		}catch(IncorrectClaimException e) {
			throw new CustomException(TokenServiceCode.TOKEN_FOR_ACCESS_FORGED);
		}catch(JWTVerificationException e) {
			throw new CustomException(TokenServiceCode.TOKEN_FOR_ACCESS_FORGED);
		}catch(Exception e) {
			throw new CustomException(DefaultServiceCode.INTERNAL_SERVER_ERROR);
		}
		
		try {
			if(oldMemberRefreshToken==null) {
				throw new CustomException(TokenServiceCode.TOKEN_FOR_REFRESH_NOT_FOUND);
			}
			
			if(tokenUtil.isExpiredWithDecoding(oldMemberRefreshToken)) {
				throw new CustomException(TokenServiceCode.TOKEN_FOR_REFRESH_EXPIRED);
			}
			
			if(!tokenUtil.isRefreshTokenWithDecoding(oldMemberRefreshToken)) {
				throw new CustomException(TokenServiceCode.TOKEN_NOT_FOR_REFRESH);
			}
			
			if(redisUtil.getString(oldMemberRefreshToken)!=null) {
				throw new CustomException(TokenServiceCode.TOKEN_FOR_REFRESH_INVALIDATED);
			}
			
			tokenUtil.verify(oldMemberRefreshToken);
			
		}catch(CustomException e) {
			throw e;
		}catch(AlgorithmMismatchException e) {
			throw new CustomException(TokenServiceCode.TOKEN_FOR_REFRESH_FORGED);
		}catch(SignatureVerificationException e) {
			throw new CustomException(TokenServiceCode.TOKEN_FOR_REFRESH_FORGED);
		}catch(TokenExpiredException e) {
			throw new CustomException(TokenServiceCode.TOKEN_FOR_REFRESH_EXPIRED);
		}catch(MissingClaimException e) {
			throw new CustomException(TokenServiceCode.TOKEN_FOR_REFRESH_FORGED);
		}catch(IncorrectClaimException e) {
			throw new CustomException(TokenServiceCode.TOKEN_FOR_REFRESH_FORGED);
		}catch(JWTVerificationException e) {
			throw new CustomException(TokenServiceCode.TOKEN_FOR_REFRESH_FORGED);
		}catch(Exception e) {
			throw new CustomException(DefaultServiceCode.INTERNAL_SERVER_ERROR);
		}
		
		if(!tokenUtil.getMemberIDWithDecoding(oldMemberAccessToken).equals(tokenUtil.getMemberIDWithDecoding(oldMemberRefreshToken))) {
			throw new CustomException(TokenServiceCode.TOKEN_OWNER_NOT_MATCHED_TO_EACH_OTHER);
		}
		
		requestUpdateMemberTokens(tokenUtil.getMemberIDWithDecoding(oldMemberAccessToken),UpdateMemberTokensRequestDTO.builder()
			.memberAccessToken(null)
			.memberRefreshToken(null)
			.build()
		);
		
		redisUtil.setString(oldMemberAccessToken, "invalidated", tokenUtil.getRemainingTimeWithDecoding(oldMemberAccessToken));
		redisUtil.setString(oldMemberRefreshToken, "invalidated", tokenUtil.getRemainingTimeWithDecoding(oldMemberRefreshToken));
	}
	
	private MemberDTO requestMemberByMemberID(String memberID) {
		try {
			return  restTemplate.getForObject(MEMBER_SERVICE_BASE_URI+"/members/"+memberID+"?member-statuses=NORMAL", ReadMemberResponseDTO.class).getData();
		}catch(Exception e) {
			return null;
		}
	}
	
	private void requestUpdateMemberTokens(String memberID, UpdateMemberTokensRequestDTO dto) {
		restTemplate.put(MEMBER_SERVICE_BASE_URI+"/members/"+memberID+"/tokens",dto);
		return;
	}
	
}
