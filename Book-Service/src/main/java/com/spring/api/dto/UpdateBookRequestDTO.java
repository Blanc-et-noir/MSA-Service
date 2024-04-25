package com.spring.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.api.enumeration.BookCategory;
import com.spring.api.enumeration.BookQuality;
import com.spring.api.enumeration.BookStatus;

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
public class UpdateBookRequestDTO {
	@JsonProperty("book-name")
	private String bookName;
	
	@JsonProperty("book-publisher-name")
	private String bookPublisherName;
	
	@JsonProperty("book-category")
	private BookCategory bookCategory;
	
	@JsonProperty("book-quality")
	private BookQuality bookQuality;
	
	@JsonProperty("book-status")
	private BookStatus bookStatus;
	
	@JsonProperty("book-price")
	private Long bookPrice;
	
	@JsonProperty("book-place")
	private String bookPlace;
	
	@JsonProperty("book-description")
	private String bookDescription;
}
