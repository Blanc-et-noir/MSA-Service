package com.spring.api.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.spring.api.dto.CreateBookImageResponseDTO;
import com.spring.api.dto.CreateBookRequestDTO;
import com.spring.api.dto.CreateBookResponseDTO;
import com.spring.api.dto.ReadBooksRequestDTO;
import com.spring.api.dto.ReadBooksResponseDTO;
import com.spring.api.dto.UpdateBookRequestDTO;

public interface BookService {
	public CreateBookResponseDTO createBook(String memberID, CreateBookRequestDTO dto);
	public void updateBook(String memberID, Long bookID, UpdateBookRequestDTO dto);
	public CreateBookImageResponseDTO createBookImage(String memberID, Long bookID, MultipartFile bookImageFile);
	public void deleteBookImage(String memberID, Long bookID, Long bookImageID);
	public ReadBooksResponseDTO readBooks(ReadBooksRequestDTO dto);
	public ResponseEntity<byte[]> readBookImage(Long bookID, Long bookImageID);
}
