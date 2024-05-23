package com.spring.api.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ReservationServiceCode implements Code{
	RESERVATION_STATUS_NOT_AVAILABLE("RESERVATION_STATUS_NOT_AVAILABLE","해당 예약 상태는 사용하실 수 없습니다.",HttpStatus.BAD_REQUEST),
	RESERVATION_ALREADY_EXISTS("RESERVATION_ALREADY_EXISTS","해당 도서에 대한 예약정보가 이미 존재합니다.",HttpStatus.BAD_REQUEST);
	
	private String code;
	private String message;
	private HttpStatus status; 
	
	ReservationServiceCode(String code, String message, HttpStatus status){
		this.code = code;
		this.message = message;
		this.status = status;
	}
}
