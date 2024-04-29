package com.spring.api.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.spring.api.code.BookServiceCode;
import com.spring.api.exception.CustomException;

public enum BookCategory {
	HUMANITIES,
	POLITICS,
	ECONOMICS,
	NATURAL_SCIENCE,
	TECHNICAL_ENGINEERING,
	COMPUTER_ENGINEERING,
	ARTS_PHYSICAL_EDUCATION,
	FOREIGN_LANGUAGES,
	ETC;
	
	@JsonCreator
    public static BookCategory from(String bookCategory) {
		try {
			return BookCategory.valueOf(bookCategory.toUpperCase());
		}catch(Exception e) {
			throw new CustomException(BookServiceCode.BOOK_CATEGORY_NOT_AVAILABLE);
		}
    }
}
