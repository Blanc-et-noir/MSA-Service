package com.spring.api.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;

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
    public static BookCategory from(String memberRole) {
        return BookCategory.valueOf(memberRole.toUpperCase());
    }
}
