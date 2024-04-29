package com.spring.api.service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spring.api.code.BookServiceCode;
import com.spring.api.dto.CreateBookImageResponseDTO;
import com.spring.api.dto.CreateBookRequestDTO;
import com.spring.api.dto.CreateBookResponseDTO;
import com.spring.api.dto.UpdateBookRequestDTO;
import com.spring.api.entity.BookEntity;
import com.spring.api.entity.BookImageEntity;
import com.spring.api.enumeration.BookImageExtension;
import com.spring.api.enumeration.BookImageStatus;
import com.spring.api.enumeration.BookStatus;
import com.spring.api.exception.CustomException;
import com.spring.api.repository.BookImageRepository;
import com.spring.api.repository.BookRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service("bookService")
public class BookServiceImpl implements BookService{
	private BookRepository bookRepository;
	private BookImageRepository bookImageRepository;
	private final String BOOK_IMAGE_PATH_PREFIX = "C:"+File.separator+"MSA-Service"+File.separator+"books";
	
	BookServiceImpl(BookRepository bookRepository,BookImageRepository bookImageRepository){
		this.bookRepository = bookRepository;
		this.bookImageRepository = bookImageRepository;
	}
	
	@Override
	public CreateBookResponseDTO createBook(String memberID, CreateBookRequestDTO dto) {
		BookEntity book = BookEntity.builder()
			.bookName(dto.getBookName())
			.bookPublisherName(dto.getBookPublisherName())
			.bookCategory(dto.getBookCategory())
			.bookStatus(BookStatus.UNREGISTERED)
			.bookCreateTime(LocalDateTime.now())
			.bookViewCount(0L)
			.bookWishCount(0L)
			.memberID(memberID)
			.build();
		
		bookRepository.save(book);
		
		return CreateBookResponseDTO.builder()
				.bookID(book.getBookID())
				.build();
	}

	@Override
	public void updateBook(String memberID, Long bookID, UpdateBookRequestDTO dto) {
		Optional<BookEntity> book = bookRepository.findById(bookID);
		
		if(book.isEmpty()||book.get().getBookStatus().equals(BookStatus.DELETED)) {
			throw new CustomException(BookServiceCode.BOOK_NOT_FOUND);
		}
		
		if(!book.get().isBookStatusEditable()) {
			throw new CustomException(BookServiceCode.BOOK_NOT_EDITABLE_DUE_TO_BOOK_STATUS);
		}
		
		if(!book.get().getMemberID().equals(memberID)) {
			throw new CustomException(BookServiceCode.BOOK_NOT_EDITABLE_DUE_TO_MEMBER_ID);
		}
		
		if(dto.getBookCategory()!=null) {
			book.get().setBookCategory(dto.getBookCategory());
		}
		
		if(dto.getBookDescription()!=null) {
			book.get().setBookDescription(dto.getBookDescription());
		}
		
		if(dto.getBookName()!=null) {
			book.get().setBookName(dto.getBookName());
		}
		
		if(dto.getBookPlace()!=null) {
			book.get().setBookPlace(dto.getBookPlace());
		}
		
		if(dto.getBookPrice()!=null) {
			book.get().setBookPrice(dto.getBookPrice());
		}
		
		if(dto.getBookPublisherName()!=null) {
			book.get().setBookPublisherName(dto.getBookPublisherName());
		}
		
		if(dto.getBookQuality()!=null) {
			book.get().setBookQuality(dto.getBookQuality());
		}
		
		if(dto.getBookStatus()!=null) {
			book.get().setBookStatus(dto.getBookStatus());
		}
	}

	@Override
	public CreateBookImageResponseDTO createBookImage(String memberID, Long bookID, MultipartFile bookImageFile) {
		Optional<BookEntity> book = bookRepository.findById(bookID);
		
		if(book.isEmpty()||book.get().getBookStatus().equals(BookStatus.DELETED)) {
			throw new CustomException(BookServiceCode.BOOK_NOT_FOUND);
		}
		
		if(!book.get().isBookStatusEditable()) {
			throw new CustomException(BookServiceCode.BOOK_NOT_EDITABLE_DUE_TO_BOOK_STATUS);
		}
		
		if(!book.get().getMemberID().equals(memberID)) {
			throw new CustomException(BookServiceCode.BOOK_NOT_EDITABLE_DUE_TO_MEMBER_ID);
		}
		
		String bookImageTemporaryName = UUID.randomUUID().toString();
		
		BookImageEntity bookImage = BookImageEntity.builder()
			.book(book.get())
			.bookImageExtension(BookImageExtension.from(getExtension(bookImageFile)))
			.bookImageStatus(BookImageStatus.NORMAL)
			.bookImageTemporaryName(bookImageTemporaryName)
			.bookImageURI(null)
			.bookImageCreateTime(LocalDateTime.now())
		.build();
		
		bookImageRepository.save(bookImage);
		
		bookImage.setBookImageURI(storeBookImageFile(bookImage.getBook().getBookID(), bookImage.getBookImageID(),bookImageTemporaryName, bookImageFile));
		
		return CreateBookImageResponseDTO.builder()
				.bookImageID(bookImage.getBookImageID())
				.build();
	}
	
	@Override
	public void deleteBookImage(String memberID, Long bookID, Long bookImageID) {
		Optional<BookEntity> book = bookRepository.findById(bookID);
		
		if(book.isEmpty()||book.get().getBookStatus().equals(BookStatus.DELETED)) {
			throw new CustomException(BookServiceCode.BOOK_NOT_FOUND);
		}
		
		if(!book.get().isBookStatusEditable()) {
			throw new CustomException(BookServiceCode.BOOK_NOT_EDITABLE_DUE_TO_BOOK_STATUS);
		}
		
		if(!book.get().getMemberID().equals(memberID)) {
			throw new CustomException(BookServiceCode.BOOK_NOT_EDITABLE_DUE_TO_MEMBER_ID);
		}
		
		Optional<BookImageEntity> bookImage = bookImageRepository.findById(bookImageID);
		
		if(bookImage.isEmpty()||bookImage.get().getBookImageStatus().equals(BookImageStatus.DELETED)) {
			throw new CustomException(BookServiceCode.BOOK_IMAGE_NOT_FOUND);
		}
		
		if(!bookImage.get().isBookImageStatusEditable()) {
			throw new CustomException(BookServiceCode.BOOK_IMAGE_NOT_EDITABLE_DUE_TO_BOOK_IMAGE_STATUS);
		}
		
		if(!bookImage.get().getBook().getBookID().equals(bookID)) {
			throw new CustomException(BookServiceCode.BOOK_IMAGE_NOT_INCLUDED_IN_BOOK);
		}
		
		book.get().deleteBookImage(bookImage.get());
	}

	private String getExtension(MultipartFile multipartFile) {		
	    return FilenameUtils.getExtension(multipartFile.getOriginalFilename());
	}
	
	private String storeBookImageFile(Long bookID, Long bookImageID, String bookImageTemporaryName, MultipartFile bookImageFile) {
		File dest = new File(BOOK_IMAGE_PATH_PREFIX+File.separator+bookID+File.separator+"book-images"+File.separator+bookImageID+File.separator+bookImageTemporaryName+"."+getExtension(bookImageFile));
		
		try {
			dest.mkdirs();
			bookImageFile.transferTo(dest);
			return dest.getAbsolutePath();
		}catch(Exception e) {
			if(dest.exists()) {
				dest.delete();
			}
			
			throw new CustomException(BookServiceCode.BOOK_IMAGE_NOT_UPLOADED_DUE_TO_ERROR);
		}
	}
	
}
