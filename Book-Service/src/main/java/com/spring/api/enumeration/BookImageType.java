package com.spring.api.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.spring.api.code.BookServiceCode;
import com.spring.api.exception.CustomException;

public enum BookImageType {
	ORIGINAL,
	THUMBNAIL;
	
	@JsonCreator
    public static BookImageType from(String bookImageExtension) {
		try {
			return BookImageType.valueOf(bookImageExtension.toUpperCase());
		}catch(Exception e) {
			throw new CustomException(BookServiceCode.BOOK_IMAGE_TYPE_NOT_AVAILABLE);
		}
    }
}
