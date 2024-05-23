package com.spring.api.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.spring.api.code.BookServiceCode;
import com.spring.api.exception.CustomException;

import lombok.Getter;

@Getter
public enum BookImageStatus {
	UNREGISTERED,
	DELETED,
	SUSPENDED,
	NORMAL;
	
	@JsonCreator
    public static BookImageStatus from(String bookImageStatus) {
        try {
        	return BookImageStatus.valueOf(bookImageStatus.toUpperCase());
		}catch(Exception e) {
			throw new CustomException(BookServiceCode.BOOK_IMAGE_STATUS_NOT_AVAILABLE);
		}
    }
}
