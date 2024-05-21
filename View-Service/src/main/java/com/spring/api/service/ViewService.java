package com.spring.api.service;

import com.spring.api.dto.BookDTO;
import com.spring.api.dto.ReadBookRequestDTO;

public interface ViewService {
	public BookDTO readBook(ReadBookRequestDTO readBookRequestDTO);
}
