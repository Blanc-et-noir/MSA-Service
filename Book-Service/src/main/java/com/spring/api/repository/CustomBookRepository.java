package com.spring.api.repository;

import java.util.List;

import com.spring.api.dto.ReadBooksRequestDTO;
import com.spring.api.entity.BookEntity;

public interface CustomBookRepository {
	public List<BookEntity> findByConditions(ReadBooksRequestDTO dto);
}
