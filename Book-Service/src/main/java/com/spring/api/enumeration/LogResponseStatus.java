package com.spring.api.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum LogResponseStatus {
	SUCCESS,
	FAIL;
	
	@JsonCreator
    public static LogResponseStatus from(String logResposneStatus) {
        return LogResponseStatus.valueOf(logResposneStatus.toUpperCase());
    }
}
