package com.spring.api.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.spring.api.code.BookServiceCode;
import com.spring.api.code.ReservationServiceCode;
import com.spring.api.dto.BookDTO;
import com.spring.api.dto.CreateReservationRequestDTO;
import com.spring.api.dto.ReadBookResponseDTO;
import com.spring.api.entity.ReservationEntity;
import com.spring.api.enumeration.BookStatus;
import com.spring.api.enumeration.ReservationStatus;
import com.spring.api.exception.CustomException;
import com.spring.api.repository.ReservationRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service("reservationService")
public class ReservationServiceImpl implements ReservationService{
	private final String BOOK_SERVICE_BASE_URI = "http://localhost:3004/api/v1";
	private RestTemplate restTemplate;
	private ReservationRepository reservationRepository;
	
	ReservationServiceImpl(RestTemplate restTemplate, ReservationRepository reservationRepository){
		this.restTemplate = restTemplate;
		this.reservationRepository = reservationRepository;
	}
	
	@Override
	public void createReservation(String memberID, CreateReservationRequestDTO dto) {
		//도서정보조회
		BookDTO book = requestBookByBookID(dto.getBookID());
		
		if(book==null) {
			throw new CustomException(BookServiceCode.BOOK_NOT_FOUND);
		}
		
		if(book.getBookStatus().equals(BookStatus.TRANSACTED)||book.getBookStatus().equals(BookStatus.TRANSACTING)) {
			throw new CustomException(BookServiceCode.BOOK_NOT_RESERVABLE_DUE_TO_BOOK_STATUS);
		}
		
		//자신의 도서인지 확인
		if(book.getMemberID().equals(memberID)) {
			throw new CustomException(BookServiceCode.BOOK_NOT_RESERVABLE_DUE_TO_MEMBER_ID);
		}
		
		//이미 예약정보 있는지 확인
		Optional<ReservationEntity> reservation = reservationRepository.findByBookIDAndMemberID(dto.getBookID(), memberID);
		
		if(reservation.isPresent()) {
			throw new CustomException(ReservationServiceCode.RESERVATION_ALREADY_EXISTS);
		}
		
		reservationRepository.save(
				ReservationEntity.builder()
				.bookID(dto.getBookID())
				.reservationStatus(ReservationStatus.REQUESTED)
				.reservationRequestTime(LocalDateTime.now())
				.memberID(memberID)
				.build());
	}
	
	private BookDTO requestBookByBookID(Long bookID) {
		try {
			return restTemplate.getForObject(BOOK_SERVICE_BASE_URI+"/books/"+bookID+"?book-statuses=TRANSACTING&book-statuses=TRANSACTED&book-statuses=NORMAL", ReadBookResponseDTO.class).getData();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
