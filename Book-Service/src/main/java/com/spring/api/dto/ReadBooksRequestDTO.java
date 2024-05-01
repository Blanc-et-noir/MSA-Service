package com.spring.api.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.api.enumeration.BookCategory;
import com.spring.api.enumeration.BookImageStatus;
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
public class ReadBooksRequestDTO {
	@JsonProperty("book-categories")
	private List<BookCategory> bookCategories;
	
	@JsonProperty("book-qualities")
	private List<BookQuality> bookQualities;
	
	@JsonProperty("book-statuses")
	private List<BookStatus> bookStatuses;
	
	@JsonProperty("book-image-statuses")
	private List<BookImageStatus> bookImageStatuses;
	
	@JsonProperty("book-min-price")
	private Long bookMinPrice;

	@JsonProperty("book-max-price")
	private Long bookMaxPrice;
	
	@JsonProperty("book-name")
	private String bookName;
	
	@JsonProperty("book-publisher-name")
	private String bookPublisherName;
	
	@JsonProperty("book-id")
	private Long bookID;
	
	@JsonProperty("limit")
	private Integer limit;
}
