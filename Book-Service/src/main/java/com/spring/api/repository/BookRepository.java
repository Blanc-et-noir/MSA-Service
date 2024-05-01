package com.spring.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.api.entity.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity, Long>, CustomBookRepository{
	
}
