package com.spring.api.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationRequestDTO {
	@JsonProperty("book-id")
	private Long bookID;
	
	@JsonProperty("reservation-description")
	private String reservationDescription;
	
	@JsonProperty("reservation-wish-time")
	private LocalDateTime reservationWishTime;
}
