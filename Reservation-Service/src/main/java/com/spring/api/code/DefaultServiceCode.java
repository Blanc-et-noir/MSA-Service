package com.spring.api.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum DefaultServiceCode implements Code{
	INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR","내부 서버에서 에러가 발생했습니다.",HttpStatus.BAD_REQUEST);
	
	private String code;
	private String message;
	private HttpStatus status; 
	
	DefaultServiceCode(String code, String message, HttpStatus status){
		this.code = code;
		this.message = message;
		this.status = status;
	}
}
