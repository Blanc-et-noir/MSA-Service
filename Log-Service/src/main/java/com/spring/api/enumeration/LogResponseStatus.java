package com.spring.api.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.spring.api.code.LogServiceCode;
import com.spring.api.exception.CustomException;

import lombok.Getter;

@Getter
public enum LogResponseStatus {
	SUCCESS,
	FAIL;
	
	@JsonCreator
    public static LogResponseStatus from(String logResposneStatus) {
		try {
			return LogResponseStatus.valueOf(logResposneStatus.toUpperCase());
		}catch(Exception e) {
			throw new CustomException(LogServiceCode.LOG_RESPONSE_STATUS_NOT_AVAILABLE);
		}        
    }
}
