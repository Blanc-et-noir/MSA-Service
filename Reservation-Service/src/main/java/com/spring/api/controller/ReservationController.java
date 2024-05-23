package com.spring.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.api.dto.CreateReservationRequestDTO;
import com.spring.api.dto.ResponseDTO;
import com.spring.api.service.ReservationService;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {
	private ReservationService reservationService;
	
	ReservationController(ReservationService reservationService){
		this.reservationService = reservationService;
	}
	
	@PostMapping("")
	public ResponseDTO createReservation(@RequestHeader(name="Member-id") String memberID, @RequestBody CreateReservationRequestDTO dto) {
		reservationService.createReservation(memberID, dto);
		return ResponseDTO.success("예약 생성 성공");
	}
}
