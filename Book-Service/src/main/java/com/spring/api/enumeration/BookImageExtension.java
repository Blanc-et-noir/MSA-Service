package com.spring.api.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum BookImageExtension {
	JPG,
	JPEG,
	PNG;
	
	@JsonCreator
    public static BookImageExtension from(String bookImageExtension) {
        return BookImageExtension.valueOf(bookImageExtension.toUpperCase());
    }
}
