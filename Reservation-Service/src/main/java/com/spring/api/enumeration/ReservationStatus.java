package com.spring.api.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.spring.api.code.ReservationServiceCode;
import com.spring.api.exception.CustomException;

import lombok.Getter;

@Getter
public enum ReservationStatus {
	REQUESTED,
	RECEIVED,
	DELETED,
	COMPLETED;
	
	@JsonCreator
    public static ReservationStatus from(String reservationStatus) {
		try {
			return ReservationStatus.valueOf(reservationStatus.toUpperCase());
		}catch(Exception e) {
			throw new CustomException(ReservationServiceCode.RESERVATION_STATUS_NOT_AVAILABLE);
		}        
    }
}
