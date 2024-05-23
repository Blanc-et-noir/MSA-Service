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
	
	@GetMapping("/join")
	public String join() {
		return "/join";
	}
	
	@GetMapping("/login")
	public String login() {
		return "/login";
	}
	
	@GetMapping("/logout")
	public String logout() {
		return "/logout";
	}
	
	@GetMapping("/error")
	public String error() {
		return "/error";
	}
	
	@GetMapping("/withdraw")
	public String withdraw() {
		return "/withdraw";
	}
	
	@GetMapping("/find")
	public String find() {
		return "/find";
	}
	
	@GetMapping("/intro")
	public String intro() {
		return "/intro";
	}
	
	@GetMapping("/board")
	public String board() {
		return "/board";
	}
	
	@GetMapping("/manage/books")
	public String manageBooks() {
		return "/manage-books";
	}
	
	@GetMapping("/manage/reservations")
	public String manageReservations() {
		return "/manage-reservations";
	}
	
	@GetMapping("/account")
	public String account() {
		return "/account";
	}
	
	@GetMapping("/report")
	public String report() {
		return "/report";
	}
	
	@GetMapping("/register")
	public String register() {
		return "/register";
	}
	
	@GetMapping("/books/{book-ids}")
	public ModelAndView books(
		@PathVariable("book-ids") Long bookID,
		@RequestParam(name="book-statuses", required=false) LinkedList<BookStatus> bookStatuses,
		@RequestParam(name="book-image-statuses", required=false) LinkedList<BookImageStatus> bookImageStatuses) {
		
		BookDTO book = viewService.readBook(ReadBookRequestDTO.builder()
				.bookID(bookID)
				.bookImageStatuses(bookImageStatuses)
				.bookStatuses(bookStatuses)
				.build());
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/books");
		modelAndView.addObject("book", book);	

		return modelAndView;
	}
	
}
