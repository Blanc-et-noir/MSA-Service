package com.spring.api.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum TokenServiceCode implements Code{
	TOKEN_FOR_ACCESS_NOT_FOUND("TOKEN_FOR_ACCESS_NOT_FOUND","액세스 토큰 정보가 존재하지 않습니다.",HttpStatus.BAD_REQUEST),
	TOKEN_FOR_REFRESH_NOT_FOUND("TOKEN_FOR_REFRESH_NOT_FOUND","리프레쉬 토큰 정보가 존재하지 않습니다.",HttpStatus.BAD_REQUEST),
	TOKEN_FOR_ACCESS_EXPIRED("TOKEN_FOR_ACCESS_EXPIRED","해당 액세스 토큰은 만료되었습니다.",HttpStatus.BAD_REQUEST),
	TOKEN_FOR_REFRESH_EXPIRED("TOKEN_FOR_REFRESH_EXPIRED","해당 리프레쉬 토큰은 만료되었습니다.",HttpStatus.BAD_REQUEST),
	TOKEN_NOT_FOR_ACCESS("TOKEN_NOT_FOR_ACCESS","해당 토큰은 액세스 토큰이 아닙니다.",HttpStatus.BAD_REQUEST),
	TOKEN_NOT_FOR_REFRESH("TOKEN_NOT_FOR_REFRESH","해당 토큰은 리프레쉬 토큰이 아닙니다.",HttpStatus.BAD_REQUEST),
	TOKEN_FOR_ACCESS_INVALIDATED("TOKEN_FOR_ACCESS_INVALIDATED","해당 액세스 토큰은 이미 무효화 처리되었습니다.",HttpStatus.BAD_REQUEST),
	TOKEN_FOR_REFRESH_INVALIDATED("TOKEN_FOR_REFRESH_INVALIDATED","해당 리프레쉬 토큰은 이미 무효화 처리되었습니다.",HttpStatus.BAD_REQUEST),
	TOKEN_FOR_ACCESS_FORGED("TOKEN_FOR_ACCESS_FORGED","해당 액세스 토큰은 위조되었습니다.",HttpStatus.BAD_REQUEST),
	TOKEN_FOR_REFRESH_FORGED("TOKEN_FOR_REFRESH_FORGED","해당 리프레쉬 토큰은 위조되었습니다.",HttpStatus.BAD_REQUEST),
	TOKEN_OWNER_NOT_MATCHED_TO_EACH_OTHER("TOKEN_OWNER_NOT_MATCHED_TO_EACH_OTHER","두 토큰의 소유주 정보가 서로 일치하지 않습니다.",HttpStatus.BAD_REQUEST),
	TOKEN_TYPE_NOT_AVAILABLE("TOKEN_TYPE_NOT_AVAILABLE","해당 토큰 타입은 사용하실 수 없습니다.",HttpStatus.BAD_REQUEST);
	
	private String code;
	private String message;
	private HttpStatus status; 
	
	TokenServiceCode(String code, String message, HttpStatus status){
		this.code = code;
		this.message = message;
		this.status = status;
	}
}
