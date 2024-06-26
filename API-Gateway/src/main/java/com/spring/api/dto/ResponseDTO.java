package com.spring.api.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.api.code.Code;

import lombok.Getter;

@Getter
public class ResponseDTO<T> {
	@JsonIgnore
	private Code code;
	
	@JsonProperty("code")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String errorCode;
	
	@JsonProperty("message")
	private String message;
	
	@JsonProperty("data")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T data;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonProperty("time")
	private LocalDateTime time;
	
	private ResponseDTO(String message){
		this.message = message;
		this.time = LocalDateTime.now();
	}
	
	private ResponseDTO(String message, T data){
		this.message = message;
		this.data = data;
		this.time = LocalDateTime.now();
	}
	
	private ResponseDTO(Code code){
		this.code = code;
		this.errorCode = code.getCode();
		this.message = code.getMessage();
		this.time = LocalDateTime.now();
	}
	
	public static <T> ResponseDTO<?> success(String message) {
		return new ResponseDTO<T>(message, null);
	}
	
	public static <T> ResponseDTO<T> success(String message, T data) {
		return new ResponseDTO<T>(message, data);
	}
	
	public static <T> ResponseDTO<?> fail(Code code) {
		return new ResponseDTO<T>(code);
	}
	
}
