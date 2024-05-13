package com.spring.api.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.spring.api.code.BookServiceCode;
import com.spring.api.exception.CustomException;

import lombok.Getter;

@Getter
public enum BookStatus {
	UNREGISTERED,
	DELETED,
	SUSPENDED,
	NORMAL;
	
	@JsonCreator
    public static BookStatus from(String bookStatus) {
		try {
			return BookStatus.valueOf(bookStatus.toUpperCase());
		}catch(Exception e) {
			throw new CustomException(BookServiceCode.BOOK_STATUS_NOT_AVAILABLE);
		}        
    }
}
