package com.spring.api.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class ReadBooksResponseDTO {
	@JsonProperty("book-id")
	private Long bookID;
	
	@JsonProperty("books")
	private List<BookDTO> books;
}
