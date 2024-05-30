package com.spring.api.entity;

import java.time.LocalDateTime;

import com.spring.api.enumeration.ReservationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="reservation")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
	name = "reservation_seq_generator",
	sequenceName = "reservation_seq",
	initialValue = 1,
	allocationSize = 100
)
public class ReservationEntity {
	@Id
	@Column(name="reservation_id")
	@GeneratedValue(
		strategy = GenerationType.SEQUENCE,
		generator = "reservation_seq_generator"
	)
	private Long reservationID;
	
	@Enumerated(EnumType.STRING)
	@Column(name="reservation_status")
	private ReservationStatus reservationStatus;
	
	@Column(name="reservation_request_time")
	private LocalDateTime reservationRequestTime;
	
	@Column(name="reservation_receive_time")
	private LocalDateTime reservationReceiveTime;
	
	@Column(name="reservation_complete_time")
	private LocalDateTime reservationCompleteTime;
	
	@Column(name="reservation_delete_time")
	private LocalDateTime reservationDeleteTime;
	
	@Column(name="reservation_wish_time")
	private LocalDateTime reservationWishTime;
	
	@Column(name="reservation_description")
	private String reservationDescription;
	
	@Column(name="book_id")
	private Long bookID;
	
	@Column(name="buyer_member_id")
	private String buyerMemberID;
	
	@Column(name="seller_member_id")
	private String sellerMemberID;
}
