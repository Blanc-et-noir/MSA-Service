package com.spring.api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.spring.api.code.BookServiceCode;
import com.spring.api.dto.BookDTO;
import com.spring.api.dto.ReadBookResponseDTO;
import com.spring.api.enumeration.BookStatus;
import com.spring.api.exception.CustomException;

@Service("viewService")
public class ViewServiceImpl implements ViewService{
	private final String BOOK_SERVICE_BASE_URI = "http://localhost:3004/api/v1";
	private RestTemplate restTemplate;
	
	ViewServiceImpl(RestTemplate restTemplate){
		this.restTemplate = restTemplate;
	}
	
	@Override
	public BookDTO readBook(Long bookID) {
		BookDTO dto = requestBook(bookID);
		
		return dto;
	}
	
	private BookDTO requestBook(Long bookID) {
		BookDTO dto = restTemplate.getForObject(BOOK_SERVICE_BASE_URI+"/books/"+bookID, ReadBookResponseDTO.class).getData();
		
		if(dto==null||!dto.getBookStatus().equals(BookStatus.NORMAL)) {
			throw new CustomException(BookServiceCode.BOOK_NOT_FOUND);
		}
		
		return dto;
	}

}
