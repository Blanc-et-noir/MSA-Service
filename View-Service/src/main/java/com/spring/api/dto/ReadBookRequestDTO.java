package com.spring.api.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.api.enumeration.BookImageStatus;
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
public class ReadBookRequestDTO {	
	@JsonProperty("book-statuses")
	private List<BookStatus> bookStatuses;
	
	@JsonProperty("book-image-statuses")
	private List<BookImageStatus> bookImageStatuses;
	
	@JsonProperty("book-id")
	private Long bookID;
}
