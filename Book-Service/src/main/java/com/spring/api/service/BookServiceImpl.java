package com.spring.api.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spring.api.code.BookServiceCode;
import com.spring.api.dto.BookDTO;
import com.spring.api.dto.BookImageDTO;
import com.spring.api.dto.CreateBookImageResponseDTO;
import com.spring.api.dto.CreateBookRequestDTO;
import com.spring.api.dto.CreateBookResponseDTO;
import com.spring.api.dto.ReadBookRequestDTO;
import com.spring.api.dto.ReadBooksRequestDTO;
import com.spring.api.dto.ReadBooksResponseDTO;
import com.spring.api.dto.UpdateBookRequestDTO;
import com.spring.api.entity.BookEntity;
import com.spring.api.entity.BookImageEntity;
import com.spring.api.enumeration.BookImageExtension;
import com.spring.api.enumeration.BookImageStatus;
import com.spring.api.enumeration.BookImageType;
import com.spring.api.enumeration.BookStatus;
import com.spring.api.exception.CustomException;
import com.spring.api.repository.BookImageRepository;
import com.spring.api.repository.BookRepository;

import jakarta.transaction.Transactional;
import net.coobird.thumbnailator.Thumbnailator;

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
		
		if(dto.getBookDetailedPlace()!=null) {
			book.get().setBookDetailedPlace(dto.getBookDetailedPlace());
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
			.bookImageURL(null)
			.bookImageCreateTime(LocalDateTime.now())
		.build();
		
		bookImageRepository.save(bookImage);
		
		storeBookImageFile(bookImage.getBook().getBookID(), bookImage.getBookImageID(),bookImageTemporaryName, bookImageFile);
		
		bookImage.setBookImageURL("http://localhost:3000/api/v1/books/"+bookID+"/book-images/"+bookImage.getBookImageID());
		
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
	
	@Override
	public ReadBooksResponseDTO readBooks(ReadBooksRequestDTO dto) {
		if(dto.getBookMinPrice()!=null&&dto.getBookMaxPrice()!=null&&dto.getBookMinPrice()>dto.getBookMaxPrice()) {
			throw new CustomException(BookServiceCode.BOOK_MIN_PRICE_GREATER_THAN_BOOK_MAX_PRICE);
		}
		
		List<BookEntity> books = bookRepository.findByConditions(dto);
		List<BookDTO> list1 = new LinkedList<BookDTO>();
		
		Long bookID = null;
		
		for(BookEntity book : books) {			
			bookID = book.getBookID();
			
			List<BookImageDTO> list2 = new LinkedList<BookImageDTO>();
			
			for(BookImageEntity bookImage : book.getBookImages()) {
				
				BookImageDTO bookImageDTO = BookImageDTO.builder()
						.bookImageID(bookImage.getBookImageID())
						.bookImageCreateTime(bookImage.getBookImageCreateTime())
						.bookImageTemporaryName(bookImage.getBookImageTemporaryName())
						.bookImageStatus(bookImage.getBookImageStatus())
						.bookImageExtension(bookImage.getBookImageExtension())
						.bookImageURL(bookImage.getBookImageURL())
						.build();
				
				if(dto.getBookImageStatuses()!=null&&!dto.getBookImageStatuses().isEmpty()) {
					for(BookImageStatus bookImageStatus : dto.getBookImageStatuses()) {
						
						if(bookImageStatus.equals(bookImage.getBookImageStatus())) {
							list2.add(bookImageDTO);
							break;
						}
					}
				}else {
					list2.add(bookImageDTO);
				}
			}
			
			list1.add(
				BookDTO.builder()
				.bookCategory(book.getBookCategory())
				.bookCreateTime(book.getBookCreateTime())
				.bookID(book.getBookID())
				.bookImages(list2)
				.bookName(book.getBookName())
				.bookPlace(book.getBookPlace())
				.bookDetailedPlace(book.getBookDetailedPlace())
				.bookPrice(book.getBookPrice())
				.bookPublisherName(book.getBookPublisherName())
				.bookQuality(book.getBookQuality())
				.bookViewCount(book.getBookViewCount())
				.bookWishCount(book.getBookWishCount())
				.memberID(book.getMemberID())
				.bookStatus(book.getBookStatus())
				.build());
		}
		
		return ReadBooksResponseDTO.builder()
				.bookID(bookID)
				.books(list1)
				.build();
	}

	@Override
	public ResponseEntity<byte[]> readBookImage(Long bookID, Long bookImageID, BookImageType bookImageType) {
		Optional<BookEntity> book = bookRepository.findById(bookID);
		
		if(book.isEmpty()) {
			throw new CustomException(BookServiceCode.BOOK_NOT_FOUND);
		}
		
		Optional<BookImageEntity> bookImage = book.get().getBookImage(bookImageID);
		
		if(bookImage.isEmpty()) {
			throw new CustomException(BookServiceCode.BOOK_IMAGE_NOT_FOUND);
		}
		
		try {
			File file = new File(BOOK_IMAGE_PATH_PREFIX+File.separator+
					bookID+File.separator+
					"book-images"+File.separator+
					bookImageID+File.separator+
					bookImage.get().getBookImageTemporaryName()+"."+bookImage.get().getBookImageExtension().toString().toLowerCase());
			
			
			byte[] bytes = null;
			
			if(bookImageType==null||bookImageType.equals(BookImageType.ORIGINAL)) {
				InputStream inputStream = new FileInputStream(file);
				bytes = IOUtils.toByteArray(inputStream);
				inputStream.close();
			}else {
				BufferedImage bufferedImage = Thumbnailator.createThumbnail(file, 200, 200);
				bytes = toByteArray(bufferedImage, bookImage.get().getBookImageExtension().toString().toLowerCase());
			}
			
			HttpHeaders headers = new HttpHeaders();
			headers.setCacheControl(CacheControl.noCache().getHeaderValue());
			
			if(bookImage.get().getBookImageExtension().equals(BookImageExtension.JPG)||bookImage.get().getBookImageExtension().equals(BookImageExtension.JPEG)) {
				headers.setContentType(MediaType.IMAGE_JPEG);
			}else if(bookImage.get().getBookImageExtension().equals(BookImageExtension.PNG)){
				headers.setContentType(MediaType.IMAGE_PNG);
			}
			
			ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, headers, HttpStatus.OK);
			return responseEntity;
		} catch (Exception e) {
			throw new CustomException(BookServiceCode.BOOK_IMAGE_NOT_DOWNLOADED_DUE_TO_ERROR);
		}
	}
	
	@Override
	public BookDTO readBook(ReadBookRequestDTO dto) {
		Optional<BookEntity> book = bookRepository.findById(dto.getBookID());
		
		if(book.isEmpty()) {
			throw new CustomException(BookServiceCode.BOOK_NOT_FOUND);
		}
		
		if(dto.getBookStatuses()!=null&&!dto.getBookImageStatuses().isEmpty()) {
			if(!dto.getBookImageStatuses().contains(book.get().getBookStatus())) {
				throw new CustomException(BookServiceCode.BOOK_NOT_FOUND);
			}
		}
		
		book.get().increaseBookViewCount();
		
		List<BookImageDTO> list = new LinkedList<BookImageDTO>();
		
		for(BookImageEntity bookImage : book.get().getBookImages()) {
			if(dto.getBookImageStatuses()!=null&&!dto.getBookImageStatuses().isEmpty()) {
				if(!dto.getBookImageStatuses().contains(bookImage.getBookImageStatus())){
					continue;
				}
			}
			
			list.add(BookImageDTO.builder()
				.bookImageCreateTime(bookImage.getBookImageCreateTime())
				.bookImageExtension(bookImage.getBookImageExtension())
				.bookImageID(bookImage.getBookImageID())
				.bookImageStatus(bookImage.getBookImageStatus())
				.bookImageTemporaryName(bookImage.getBookImageTemporaryName())
				.bookImageURL(bookImage.getBookImageURL())
				.build()
			);
		}
		
		return BookDTO.builder()
				.bookCategory(book.get().getBookCategory())
				.bookCreateTime(book.get().getBookCreateTime())
				.bookDetailedPlace(book.get().getBookDetailedPlace())
				.bookID(book.get().getBookID())
				.bookImages(list)
				.bookName(book.get().getBookName())
				.bookDescription(book.get().getBookDescription())
				.bookPlace(book.get().getBookPlace())
				.bookPrice(book.get().getBookPrice())
				.bookPublisherName(book.get().getBookPublisherName())
				.bookQuality(book.get().getBookQuality())
				.bookStatus(book.get().getBookStatus())
				.bookViewCount(book.get().getBookViewCount())
				.bookWishCount(book.get().getBookWishCount())
				.build();
	}
	
	private String getExtension(MultipartFile multipartFile) {		
	    return FilenameUtils.getExtension(multipartFile.getOriginalFilename());
	}
	
	private void storeBookImageFile(Long bookID, Long bookImageID, String bookImageTemporaryName, MultipartFile bookImageFile) {
		File dest = new File(BOOK_IMAGE_PATH_PREFIX+File.separator+bookID+File.separator+"book-images"+File.separator+bookImageID+File.separator+bookImageTemporaryName+"."+getExtension(bookImageFile));
		
		try {
			dest.mkdirs();
			bookImageFile.transferTo(dest);
		}catch(Exception e) {
			if(dest.exists()) {
				dest.delete();
			}
			
			throw new CustomException(BookServiceCode.BOOK_IMAGE_NOT_UPLOADED_DUE_TO_ERROR);
		}
	}
	
	private byte[] toByteArray(BufferedImage bufferedImage, String format)throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, format, outputStream);
		byte[] bytes = outputStream.toByteArray();
		outputStream.close();
		return bytes;
	}
	
}
