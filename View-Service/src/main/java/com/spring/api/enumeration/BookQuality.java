package com.spring.api.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.spring.api.code.BookServiceCode;
import com.spring.api.exception.CustomException;

import lombok.Getter;

@Getter
public enum BookQuality {
	A,
	B,
	C,
	D;
	
	@JsonCreator
    public static BookQuality from(String bookQuality) {
		try {
        	return BookQuality.valueOf(bookQuality.toUpperCase());
		}catch(Exception e) {
			throw new CustomException(BookServiceCode.BOOK_QUALITY_NOT_AVAILABLE);
		}
    }
}
