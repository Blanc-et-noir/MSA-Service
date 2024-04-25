package com.spring.api.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum BookQuality {
	A,
	B,
	C,
	D;
	
	@JsonCreator
    public static BookQuality from(String logResposneStatus) {
        return BookQuality.valueOf(logResposneStatus.toUpperCase());
    }
}
