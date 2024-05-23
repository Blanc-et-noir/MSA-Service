package com.spring.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class BookDTO {
	@JsonProperty("book-id")
	private Long bookID;
	
	@JsonProperty("book-name")
	private String bookName;
	
	@JsonProperty("book-publisher-name")
	private String bookPublisherName;
	
	@JsonProperty("book-category")
	private BookCategory bookCategory;
	
	@JsonProperty("book-status")
	private BookStatus bookStatus;
	
	@JsonProperty("book-price")
	private Long bookPrice;
	
	@JsonProperty("book-view-count")
	private Long bookViewCount;
	
	@JsonProperty("book-wish-count")
	private Long bookWishCount;
	
	@JsonProperty("book-place")
	private String bookPlace;
	
	@JsonProperty("book-detailed-place")
	private String bookDetailedPlace;
	
	@JsonProperty("book-description")
	private String bookDescription;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonProperty("book-create-time")
	private LocalDateTime bookCreateTime;
	
	@JsonProperty("member-id")
	private String memberID;
	
	@JsonProperty("book-quality")
	private BookQuality bookQuality;
	
	@JsonProperty("book-images")
	private List<BookImageDTO> bookImages;
}
