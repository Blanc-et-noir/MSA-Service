package com.spring.api.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum LogServiceCode implements Code{
	LOG_RESPONSE_STATUS_NOT_AVAILABLE("LOG_RESPONSE_STATUS_NOT_AVAILABLE","해당 로그 응답 상태는 사용하실 수 없습니다.",HttpStatus.BAD_REQUEST);
	
	private String code;
	private String message;
	private HttpStatus status; 
	
	LogServiceCode(String code, String message, HttpStatus status){
		this.code = code;
		this.message = message;
		this.status = status;
	}
}
