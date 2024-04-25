package com.spring.api.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.api.dto.CreateBookRequestDTO;
import com.spring.api.dto.ResponseDTO;
import com.spring.api.dto.UpdateBookRequestDTO;
import com.spring.api.service.BookService;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {
	private BookService bookService;
	
	BookController(BookService bookService){
		this.bookService = bookService;
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
	
	@PostMapping("/{book-ids}/book-images")
	public ResponseDTO createBookImage(@RequestHeader("Member-id") String memberID, @PathVariable("book-ids") Long bookID, @RequestPart("book-image") MultipartFile bookImageFile) {
		return ResponseDTO.success("도서 이미지 등록 성공", bookService.createBookImage(memberID,bookID, bookImageFile));
	}
	
	@DeleteMapping("/{book-ids}/book-images/{book-image-ids}")
	public ResponseDTO deleteBookImage(@RequestHeader("Member-id") String memberID, @PathVariable("book-ids") Long bookID, @PathVariable("book-image-ids") Long bookImageID) {
		bookService.deleteBookImage(memberID,bookID,bookImageID);
		return ResponseDTO.success("도서 이미지 삭제 성공");
	}
}
