package com.spring.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.api.entity.BookEntity;

@Repository("bookRepository")
public interface BookRepository extends JpaRepository<BookEntity, Long>{

}
