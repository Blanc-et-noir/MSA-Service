package com.spring.api.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum BookImageStatus {
	UNREGISTERED,
	DELETED,
	SUSPENDED,
	NORMAL;
	
	@JsonCreator
    public static BookImageStatus from(String logResposneStatus) {
        return BookImageStatus.valueOf(logResposneStatus.toUpperCase());
    }
}
