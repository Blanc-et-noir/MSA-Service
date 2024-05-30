package com.spring.api.controller;

import java.util.LinkedList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spring.api.dto.BookDTO;
import com.spring.api.dto.ReadBookRequestDTO;
import com.spring.api.enumeration.BookImageStatus;
import com.spring.api.enumeration.BookStatus;
import com.spring.api.service.ViewService;

@Controller
@RequestMapping("/api/v1/views")
public class ViewController {
	private ViewService viewService;
	
	ViewController(ViewService viewService){
		this.viewService = viewService;
	}
	
	@GetMapping("/home")
	public String home() {
		return "/home";
	}
	
	@GetMapping("/error")
	public String error() {
		return "/error";
	}
	
	@GetMapping("/create/tokens")
	public String createTokens() {
		return "/create-tokens";
	}
	
	@GetMapping("/create/members")
	public String createMembers() {
		return "/create-members";
	}	
	
	@GetMapping("/delete/members")
	public String deleteMembers() {
		return "/delete-members";
	}
	
	@GetMapping("/find/members")
	public String findMembers() {
		return "/find-members";
	}
	
	@GetMapping("/read/books")
	public String readBooks() {
		return "/read-books";
	}
	
	@GetMapping("/manage/books")
	public String manageBooks() {
		return "/manage-books";
	}
	
	@GetMapping("/manage/reservations")
	public String manageReservations() {
		return "/manage-reservations";
	}
	
	@GetMapping("/manage/members")
	public String manageMembers() {
		return "/manage-members";
	}
	
	@GetMapping("/create/reports")
	public String createReports() {
		return "/create-reports";
	}
	
	@GetMapping("/create/books")
	public String createBooks() {
		return "/create-books";
	}
	
	@GetMapping("/update/books/{book-ids}")
	public ModelAndView updateBooks(
			@PathVariable("book-ids") Long bookID,
			@RequestParam(name="book-statuses", required=false) LinkedList<BookStatus> bookStatuses,
			@RequestParam(name="book-image-statuses", required=false) LinkedList<BookImageStatus> bookImageStatuses) {
		
		BookDTO book = viewService.readBook(ReadBookRequestDTO.builder()
				.bookID(bookID)
				.bookImageStatuses(bookImageStatuses)
				.bookStatuses(bookStatuses)
				.build());
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/create-books");
		modelAndView.addObject("book", book);
		
		return modelAndView;
	}
	
	@GetMapping("/read/books/{book-ids}")
	public ModelAndView readBooks(
		@PathVariable("book-ids") Long bookID,
		@RequestParam(name="book-statuses", required=false) LinkedList<BookStatus> bookStatuses,
		@RequestParam(name="book-image-statuses", required=false) LinkedList<BookImageStatus> bookImageStatuses) {
		
		BookDTO book = viewService.readBook(ReadBookRequestDTO.builder()
				.bookID(bookID)
				.bookImageStatuses(bookImageStatuses)
				.bookStatuses(bookStatuses)
				.build());
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/read-book");
		modelAndView.addObject("book", book);	

		return modelAndView;
	}
	
}
