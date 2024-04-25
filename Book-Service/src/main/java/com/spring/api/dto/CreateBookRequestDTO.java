package com.spring.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.api.enumeration.BookCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookRequestDTO {
	@JsonProperty("book-name")
	private String bookName;
	
	@JsonProperty("book-publisher-name")
	private String bookPublisherName;
	
	@JsonProperty("book-category")
	private BookCategory bookCategory;
}
