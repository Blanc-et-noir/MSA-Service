package com.spring.api.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum BookStatus {
	UNREGISTERED,
	DELETED,
	SUSPENDED,
	NORMAL;
	
	@JsonCreator
    public static BookStatus from(String logResposneStatus) {
        return BookStatus.valueOf(logResposneStatus.toUpperCase());
    }
}
