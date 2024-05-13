package com.spring.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
	
	@GetMapping("/manage")
	public String manage() {
		return "/manage";
	}
	
	@GetMapping("/account")
	public String account() {
		return "/account";
	}
	
	@GetMapping("/reservation")
	public String reservation() {
		return "/reservation";
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
	public ModelAndView books(@PathVariable("book-ids") Long bookID) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/books");
		modelAndView.addObject("book", viewService.readBook(bookID));

		return modelAndView;
	}
	
}
