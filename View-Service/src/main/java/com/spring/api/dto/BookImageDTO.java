package com.spring.api.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.api.enumeration.BookImageExtension;
import com.spring.api.enumeration.BookImageStatus;

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
public class BookImageDTO {
	@JsonProperty("book-image-id")
	private Long bookImageID;
	
	@JsonProperty("book-image-temporary-name")
	private String bookImageTemporaryName;
	
	@JsonProperty("book-image-url")
	private String bookImageURL;
	
	@JsonProperty("book-image-extension")
	private BookImageExtension bookImageExtension;
	
	@JsonProperty("book-image-status")
	private BookImageStatus bookImageStatus;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonProperty("book-image-create-time")
	private LocalDateTime bookImageCreateTime;
}
