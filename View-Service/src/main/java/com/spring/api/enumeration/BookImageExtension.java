package com.spring.api.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.spring.api.code.BookServiceCode;
import com.spring.api.exception.CustomException;

public enum BookImageExtension {
	JPG,
	JPEG,
	PNG;
	
	@JsonCreator
    public static BookImageExtension from(String bookImageExtension) {
		try {
			return BookImageExtension.valueOf(bookImageExtension.toUpperCase());
		}catch(Exception e) {
			throw new CustomException(BookServiceCode.BOOK_IMAGE_EXTENSION_NOT_AVAILABLE);
		}
    }
}
