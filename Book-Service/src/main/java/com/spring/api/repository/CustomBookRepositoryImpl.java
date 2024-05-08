package com.spring.api.repository;

import java.util.List;

import com.spring.api.dto.ReadBooksRequestDTO;
import com.spring.api.entity.BookEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class CustomBookRepositoryImpl implements CustomBookRepository{
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<BookEntity> findByConditions(ReadBooksRequestDTO dto) {
		StringBuilder sb = new StringBuilder("");
		sb.append("SELECT b FROM BookEntity b WHERE b.bookID < :bookID");
		
		if(dto.getBookMinPrice()!=null) {
			sb.append(" AND b.bookPrice >= :bookMinPrice");
		}
		
		if(dto.getBookMaxPrice()!=null) {
			sb.append(" AND b.bookPrice <= :bookMaxPrice");
		}
		
		if(dto.getBookStatuses()!=null&&!dto.getBookStatuses().isEmpty()) {
			sb.append(" AND b.bookStatus IN :bookStatuses");
		}
		
		if(dto.getBookCategories()!=null&&!dto.getBookCategories().isEmpty()) {
			sb.append(" AND b.bookCategory IN :bookCategories");
		}
		
		if(dto.getBookQualities()!=null&&!dto.getBookQualities().isEmpty()) {
			sb.append(" AND b.bookQuality IN :bookQualities");
		}
		
		if(dto.getBookName()!=null) {
			sb.append(" AND b.bookName LIKE CONCAT('%', :bookName, '%')");
		}
		
		if(dto.getBookPublisherName()!=null) {
			sb.append(" AND b.bookPublisherName LIKE CONCAT('%', :bookPublisherName, '%')");
		}
		
		if(dto.getBookDetailedPlace()!=null) {
			sb.append(" AND b.bookDetailedPlace LIKE CONCAT('%', :bookDetailedPlace, '%')");
		}
		
		sb.append(" ORDER BY b.bookID DESC");
		
		TypedQuery<BookEntity> query = em.createQuery(sb.toString(), BookEntity.class);
		query.setParameter("bookID", dto.getBookID());
		query.setMaxResults(dto.getLimit());
		
		if(dto.getBookMinPrice()!=null) {
			query.setParameter("bookMinPrice", dto.getBookMinPrice());
		}
		
		if(dto.getBookMaxPrice()!=null) {
			query.setParameter("bookMaxPrice", dto.getBookMaxPrice());
		}
		
		if(dto.getBookStatuses()!=null&&!dto.getBookStatuses().isEmpty()) {
			query.setParameter("bookStatuses", dto.getBookStatuses());
		}
		
		if(dto.getBookCategories()!=null&&!dto.getBookCategories().isEmpty()) {
			query.setParameter("bookCategories", dto.getBookCategories());
		}
		
		if(dto.getBookQualities()!=null&&!dto.getBookQualities().isEmpty()) {
			query.setParameter("bookQualities", dto.getBookQualities());
		}
		
		if(dto.getBookName()!=null) {
			query.setParameter("bookName", dto.getBookName());
		}
		
		if(dto.getBookPublisherName()!=null) {
			query.setParameter("bookPublisherName", dto.getBookPublisherName());
		}
		
		if(dto.getBookDetailedPlace()!=null) {
			query.setParameter("bookDetailedPlace", dto.getBookDetailedPlace());
		}
		
		List<BookEntity> result = query.getResultList();
		
		return result;
	}	

}
