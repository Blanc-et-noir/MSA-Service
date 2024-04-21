package com.spring.api.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode implements Code{
	LOGIN_FAILED("LOGIN_FAILED","로그인 실패",HttpStatus.BAD_REQUEST),
	
	TOKEN_FOR_ACCESS_NOT_FOUND("TOKEN_FOR_ACCESS_NOT_FOUND","액세스 토큰이 없음",HttpStatus.BAD_REQUEST),
	TOKEN_FOR_REFRESH_NOT_FOUND("TOKEN_FOR_REFRESH_NOT_FOUND","리프레쉬 토큰이 없음",HttpStatus.BAD_REQUEST),
	TOKEN_FOR_ACCESS_EXPIRED("TOKEN_FOR_ACCESS_EXPIRED","액세스 토큰이 만료됨",HttpStatus.BAD_REQUEST),
	TOKEN_FOR_REFRESH_EXPIRED("TOKEN_FOR_REFRESH_EXPIRED","리프레쉬 토큰이 만료됨",HttpStatus.BAD_REQUEST),
	TOKEN_NOT_FOR_ACCESS("TOKEN_NOT_FOR_ACCESS","액세스 토큰이 아님",HttpStatus.BAD_REQUEST),
	TOKEN_NOT_FOR_REFRESH("TOKEN_NOT_FOR_REFRESH","리프레쉬 토큰이 아님",HttpStatus.BAD_REQUEST),
	TOKEN_FOR_ACCESS_INVALIDATED("TOKEN_FOR_ACCESS_INVALIDATED","액세스 토큰이 무효화됨",HttpStatus.BAD_REQUEST),
	TOKEN_FOR_REFRESH_INVALIDATED("TOKEN_FOR_REFRESH_INVALIDATED","리프레쉬 토큰이 무효화됨",HttpStatus.BAD_REQUEST),
	TOKEN_FOR_ACCESS_FORGED("TOKEN_FOR_ACCESS_FORGED","액세스 토큰이 위조됨",HttpStatus.BAD_REQUEST),
	TOKEN_FOR_REFRESH_FORGED("TOKEN_FOR_REFRESH_FORGED","액세스 토큰이 위조됨",HttpStatus.BAD_REQUEST),
	TOKEN_OWNER_NOT_MATCHED_TO_EACH_OTHER("TOKEN_OWNER_NOT_MATCHED_TO_EACH_OTHER","토큰 소유자가 일치하지 않음",HttpStatus.BAD_REQUEST),
	
	INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR","내부 서버 에러",HttpStatus.INTERNAL_SERVER_ERROR),
	EXTERNAL_SERVER_ERROR("EXTERNAL_SERVER_ERROR","외부 서버 에러",HttpStatus.SERVICE_UNAVAILABLE),
	
	MEMBER_NOT_FOUND("MEMBER_NOT_FOUND","회원정보 없음",HttpStatus.NOT_FOUND),
	MEMBER_EMAIL_VERIFICATION_CODE_NOT_SENT("MEMBER_EMAIL_VERIFICATION_CODE_NOT_SENT","이메일 인증코드 전송중 에러 발생",HttpStatus.INTERNAL_SERVER_ERROR),
	MEMBER_EMAIL_VERIFICATION_CODE_NOT_FOUND("MEMBER_EMAIL_VERIFICATION_CODE_NOT_FOUND","이메일 인증코드 미발급",HttpStatus.BAD_REQUEST),
	MEMBER_EMAIL_VERIFICATION_CODE_IS_WRONG("MEMBER_EMAIL_VERIFICATION_CODE_IS_WRONG","이메일 인증코드 불일치",HttpStatus.BAD_REQUEST),
	MEMBER_EMAIL_NOT_VERIFIED("MEMBER_EMAIL_NOT_VERIFIED","인증되지 않은 이메일",HttpStatus.BAD_REQUEST),
	MEMBER_ID_ALREADY_OCCUPIED("MEMBER_ID_ALREADY_OCCUPIED","이미 ID 이메일",HttpStatus.BAD_REQUEST),
	MEMBER_EMAIL_ALREADY_OCCUPIED("MEMBER_EMAIL_ALREADY_OCCUPIED","이미 사용중인 이메일",HttpStatus.BAD_REQUEST),
	MEMBER_PW_NOT_MATCHED_TO_EACH_OTHER("MEMBER_PW_NOT_MATCHED_TO_EACH_OTHER","회원 PW와 PW CHECK가 서로 일치하지 않음",HttpStatus.BAD_REQUEST),
	MEMBER_EMAIL_NOT_BOUND_TO_MEMBER_ID("MEMBER_EMAIL_NOT_BOUND_TO_MEMBER_ID","해당 회원 ID에 등록된 이메일이 아님",HttpStatus.BAD_REQUEST);
	
	private String code;
	private String message;
	private HttpStatus status; 
	
	ErrorCode(String code, String message, HttpStatus status){
		this.code = code;
		this.message = message;
		this.status = status;
	}
}
