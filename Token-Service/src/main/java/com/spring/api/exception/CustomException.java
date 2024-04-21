package com.spring.api.exception;

import com.spring.api.code.Code;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
	Code code;
	
	public CustomException(){
		super();
	}
	
	public CustomException(Code code){
		super();
		this.code = code;
	}
}
