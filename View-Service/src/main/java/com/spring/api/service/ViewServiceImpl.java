package com.spring.api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.spring.api.code.BookServiceCode;
import com.spring.api.dto.BookDTO;
import com.spring.api.dto.ReadBookRequestDTO;
import com.spring.api.dto.ReadBookResponseDTO;
import com.spring.api.enumeration.BookImageStatus;
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
	public BookDTO readBook(ReadBookRequestDTO dto) {
		BookDTO book = requestBook(dto);
		
		if(book==null) {
			throw new CustomException(BookServiceCode.BOOK_NOT_FOUND);
		}
		
		return book;
	}
	
	private BookDTO requestBook(ReadBookRequestDTO dto) {
		try {
			StringBuilder parameters = new StringBuilder("?");
			
			if(dto.getBookStatuses()!=null&&!dto.getBookStatuses().isEmpty()) {
				for(BookStatus bookStatus:dto.getBookStatuses()) {
					parameters.append("book-statuses="+bookStatus.toString()+"&");
				}
			}
			
			if(dto.getBookImageStatuses()!=null&&!dto.getBookImageStatuses().isEmpty()) {
				for(BookImageStatus bookImageStatus:dto.getBookImageStatuses()) {
					parameters.append("book-image-statuses="+bookImageStatus.toString()+"&");
				}
			}
			
			BookDTO book = restTemplate.getForObject(BOOK_SERVICE_BASE_URI+"/books/"+dto.getBookID()+parameters.toString(), ReadBookResponseDTO.class).getData();
			
			if(book==null) {
				throw new CustomException(BookServiceCode.BOOK_NOT_FOUND);
			}
			
			return book;
		}catch(Exception e) {
			return null;
		}		
	}
}
