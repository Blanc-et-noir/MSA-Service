package com.spring.api.service;

import com.spring.api.dto.CreateReservationRequestDTO;

public interface ReservationService {
	public void createReservation(String memberID, CreateReservationRequestDTO dto);
}
