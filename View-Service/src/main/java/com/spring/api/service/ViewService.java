package com.spring.api.service;

import com.spring.api.dto.BookDTO;

public interface ViewService {
	public BookDTO readBook(Long bookID);
}
