package com.spring.api.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.spring.api.code.Code;
import com.spring.api.code.DefaultServiceCode;
import com.spring.api.dto.ResponseDTO;
import com.spring.api.exception.CustomException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler({CustomException.class})
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException e){
		Code code = e.getCode();
		return new ResponseEntity(ResponseDTO.fail(code),code.getStatus());
	}
	
	@ExceptionHandler({Exception.class})
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		ResponseDTO dto = ResponseDTO.fail(DefaultServiceCode.INTERNAL_SERVER_ERROR);
		return new ResponseEntity(dto, dto.getCode().getStatus());
	}
}