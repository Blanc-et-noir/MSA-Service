package com.spring.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.api.entity.BookImageEntity;

@Repository("bookImageRepository")
public interface BookImageRepository extends JpaRepository<BookImageEntity, Long>{

}
