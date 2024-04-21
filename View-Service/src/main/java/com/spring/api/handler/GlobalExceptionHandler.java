package com.spring.api.handler;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.api.code.Code;
import com.spring.api.code.ErrorCode;
import com.spring.api.dto.response.ResponseDTO;
import com.spring.api.exception.CustomException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler({CustomException.class})
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException e){
		Code code = e.getCode();
		
		try {
			System.out.println(new ObjectMapper().writeValueAsString(ResponseDTO.fail(code)));
		}catch(Exception e2) {
			
		}
		
		return new ResponseEntity(ResponseDTO.fail(code),code.getStatus());
	}

	@ExceptionHandler({RestClientException.class})
	public ResponseEntity<ErrorResponse> handleRestClientException(RestClientException e) throws ParseException {
		JSONParser parser = new JSONParser(e.getMessage().substring(7, e.getMessage().length()-1));
		JSONObject obj = (JSONObject)parser.parse();
		
		return new ResponseEntity(obj, HttpStatus.valueOf(e.getMessage().substring(0,3)));
	}
	
	@ExceptionHandler({Exception.class})
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		e.printStackTrace();
		ResponseDTO dto = ResponseDTO.fail(ErrorCode.INTERNAL_SERVER_ERROR);
		return new ResponseEntity(dto, dto.getCode().getStatus());
	}
}