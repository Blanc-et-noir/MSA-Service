package com.spring.api.controller;

import java.util.LinkedList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.api.dto.CreateBookRequestDTO;
import com.spring.api.dto.ReadBooksRequestDTO;
import com.spring.api.dto.ResponseDTO;
import com.spring.api.dto.UpdateBookRequestDTO;
import com.spring.api.enumeration.BookCategory;
import com.spring.api.enumeration.BookImageStatus;
import com.spring.api.enumeration.BookQuality;
import com.spring.api.enumeration.BookStatus;
import com.spring.api.service.BookService;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {
	private BookService bookService;
	
	BookController(BookService bookService){
		this.bookService = bookService;
	}
	
	@GetMapping("")
	public ResponseDTO readBooks(
		@RequestParam(name="book-categories", required=false) LinkedList<BookCategory> bookCategories,
		@RequestParam(name="book-qualities", required=false) LinkedList<BookQuality> bookQualities,
		@RequestParam(name="book-statuses", required=false) LinkedList<BookStatus> bookStatuses,
		@RequestParam(name="book-image-statuses", required=false) LinkedList<BookImageStatus> bookImageStatuses,
		@RequestParam(name="book-id", required=false) Long bookID,
		@RequestParam(name="book-max-price", required=false) Long bookMaxPrice,
		@RequestParam(name="book-min-price", required=false) Long bookMinPrice,
		@RequestParam(name="book-name", required=false) String bookName,
		@RequestParam(name="book-publisher-name", required=false) String bookPublisherName,
		@RequestParam(name="book-detailed-place", required=false) String bookDetailedPlace,
		@RequestParam(name="limit", required=false) Integer limit
	) {
		ReadBooksRequestDTO dto = ReadBooksRequestDTO.builder()
				.bookDetailedPlace(bookDetailedPlace)
				.bookCategories(bookCategories)
				.bookImageStatuses(bookImageStatuses)
				.bookID(bookID)
				.bookMaxPrice(bookMaxPrice)
				.bookMinPrice(bookMinPrice)
				.bookName(bookName)
				.bookPublisherName(bookPublisherName)
				.bookQualities(bookQualities)
				.bookStatuses(bookStatuses)
				.limit(limit)
				.build();
		
		return ResponseDTO.success("도서 목록 조회 성공", bookService.readBooks(dto));
	}
	
	@PostMapping("")
	public ResponseDTO createBook(@RequestHeader("Member-id") String memberID, @RequestBody CreateBookRequestDTO dto) {
		return ResponseDTO.success("도서 등록 성공", bookService.createBook(memberID, dto));
	}
	
	@PatchMapping("/{book-ids}")
	public ResponseDTO updateBook(@RequestHeader("Member-id") String memberID, @PathVariable("book-ids") Long bookID, @RequestBody UpdateBookRequestDTO dto) {
		bookService.updateBook(memberID, bookID, dto);
		return ResponseDTO.success("도서 수정 성공");
	}
	
	@GetMapping("/{book-ids}/book-images/{book-image-ids}")
	public ResponseEntity<byte[]> readBookImage(@PathVariable("book-ids") Long bookID, @PathVariable("book-image-ids") Long bookImageID) {
		return bookService.readBookImage(bookID, bookImageID);
	}
	
	@PostMapping("/{book-ids}/book-images")
	public ResponseDTO createBookImage(@RequestHeader("Member-id") String memberID, @PathVariable("book-ids") Long bookID, @RequestPart("book-image") MultipartFile bookImageFile) {
		return ResponseDTO.success("도서 이미지 등록 성공", bookService.createBookImage(memberID,bookID, bookImageFile));
	}
	
	@DeleteMapping("/{book-ids}/book-images/{book-image-ids}")
	public ResponseDTO deleteBookImage(@RequestHeader("Member-id") String memberID, @PathVariable("book-ids") Long bookID, @PathVariable("book-image-ids") Long bookImageID) {
		bookService.deleteBookImage(memberID,bookID,bookImageID);
		return ResponseDTO.success("도서 이미지 삭제 성공");
	}
	
	@GetMapping("/{book-ids}")
	public ResponseDTO readBook(@PathVariable("book-ids") Long bookID) {
		return ResponseDTO.success("도서 수정 성공", bookService.readBook(bookID));
	}
}
