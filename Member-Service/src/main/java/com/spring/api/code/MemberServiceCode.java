package com.spring.api.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum MemberServiceCode implements Code{
	MEMBER_NOT_FOUND("MEMBER_NOT_FOUND","해당 회원 정보가 존재하지 않습니다.",HttpStatus.NOT_FOUND),
	MEMBER_EMAIL_VERIFICATION_CODE_NOT_SENT("MEMBER_EMAIL_VERIFICATION_CODE_NOT_SENT","해당 이메일 인증코드를 전송하는 과정에서 에러가 발생했습니다.",HttpStatus.INTERNAL_SERVER_ERROR),
	MEMBER_EMAIL_VERIFICATION_CODE_NOT_FOUND("MEMBER_EMAIL_VERIFICATION_CODE_NOT_FOUND","해당 이메일에 대한 인증코드 발급 정보를 찾을 수 없습니다.",HttpStatus.BAD_REQUEST),
	MEMBER_EMAIL_VERIFICATION_CODE_IS_WRONG("MEMBER_EMAIL_VERIFICATION_CODE_IS_WRONG","해당 이메일에 대한 인증 코드가 올바르지 않습니다.",HttpStatus.BAD_REQUEST),
	MEMBER_EMAIL_NOT_VERIFIED("MEMBER_EMAIL_NOT_VERIFIED","해당 이메일은 인증되지 않았습니다.",HttpStatus.BAD_REQUEST),
	MEMBER_ID_ALREADY_OCCUPIED("MEMBER_ID_ALREADY_OCCUPIED","해당 ID는 이미 다른 회원이 사용중입니다.",HttpStatus.BAD_REQUEST),
	MEMBER_EMAIL_ALREADY_OCCUPIED("MEMBER_EMAIL_ALREADY_OCCUPIED","해당 이메일은 이미 다른 회원이 사용중입니다.",HttpStatus.BAD_REQUEST),
	MEMBER_PW_NOT_MATCHED_TO_EACH_OTHER("MEMBER_PW_NOT_MATCHED_TO_EACH_OTHER","회원 PW와 PW CHECK가 서로 일치하지 않습니다.",HttpStatus.BAD_REQUEST),
	MEMBER_EMAIL_NOT_BOUND_TO_MEMBER_ID("MEMBER_EMAIL_NOT_BOUND_TO_MEMBER_ID","해당 이메일은 해당 ID와 연동된 상태가 아닙니다.",HttpStatus.BAD_REQUEST),
	MEMBER_ROLE_NOT_AVAILABLE("MEMBER_ROLE_NOT_AVAILABLE","해당 회원 역할은 사용하실 수 없습니다.",HttpStatus.BAD_REQUEST),
	MEMBER_STATUS_NOT_AVAILABLE("MEMBER_STATUS_NOT_AVAILABLE","해당 회원 상태는 사용하실 수 없습니다.",HttpStatus.BAD_REQUEST),
	OTHER_MEMBER_NOT_EDITABLE("OTHER_MEMBER_NOT_EDITABLE","다른 회원의 정보는 수정하실 수 없습니다.",HttpStatus.BAD_REQUEST);
	
	private String code;
	private String message;
	private HttpStatus status; 
	
	MemberServiceCode(String code, String message, HttpStatus status){
		this.code = code;
		this.message = message;
		this.status = status;
	}
}
