package com.spring.api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.spring.api.code.BookServiceCode;
import com.spring.api.dto.BookDTO;
import com.spring.api.dto.ReadBookRequestDTO;
import com.spring.api.dto.ReadBookResponseDTO;
import com.spring.api.exception.CustomException;

@Service("viewService")
public class ViewServiceImpl implements ViewService{
	private final String BOOK_SERVICE_BASE_URI = "http://localhost:3004/api/v1";
	private RestTemplate restTemplate;
	
	ViewServiceImpl(RestTemplate restTemplate){
		this.restTemplate = restTemplate;
	}
	
	@Override
	public BookDTO readBook(ReadBookRequestDTO dto) {
		BookDTO book = requestBook(dto);
		
		return book;
	}
	
	private BookDTO requestBook(ReadBookRequestDTO dto) {
		BookDTO book = restTemplate.getForObject(BOOK_SERVICE_BASE_URI+"/books/"+dto.getBookID(), ReadBookResponseDTO.class).getData();
		
		if(book==null) {
			throw new CustomException(BookServiceCode.BOOK_NOT_FOUND);
		}
		
		return book;
	}
}
