package com.spring.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.api.entity.ReservationEntity;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long>{
	@Query("SELECT r FROM ReservationEntity r WHERE r.sellerMemberID = :memberID AND r.bookID = :bookID")
	public Optional<ReservationEntity> findByBookIDAndSellerMemberID(@Param("bookID") Long bookID, @Param("memberID") String memberID);
	
	@Query("SELECT r FROM ReservationEntity r WHERE r.buyerMemberID = :memberID AND r.bookID = :bookID")
	public Optional<ReservationEntity> findByBookIDAndBuyerMemberID(@Param("bookID") Long bookID, @Param("memberID") String memberID);
	
	@Query("SELECT r FROM ReservationEntity r WHERE r.sellerMemberID = :memberID AND r.bookID = :bookID")
	public Optional<ReservationEntity> findBySellerMemberID(@Param("bookID") Long bookID, @Param("memberID") String memberID);
	
	@Query("SELECT r FROM ReservationEntity r WHERE r.buyerMemberID = :memberID AND r.bookID = :bookID")
	public Optional<ReservationEntity> findByBuyerMemberID(@Param("bookID") Long bookID, @Param("memberID") String memberID);
}
