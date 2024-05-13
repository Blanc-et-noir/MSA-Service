package com.spring.api.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.spring.api.enumeration.BookCategory;
import com.spring.api.enumeration.BookImageStatus;
import com.spring.api.enumeration.BookQuality;
import com.spring.api.enumeration.BookStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="book")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
	name = "book_seq_generator",
	sequenceName = "book_seq",
	initialValue = 1,
	allocationSize = 100
)
public class BookEntity {
	@Id
	@Column(name="book_id")
	@GeneratedValue(
		strategy = GenerationType.SEQUENCE,
		generator = "book_seq_generator"
	)
	private Long bookID;
	
	@Column(name="book_price")
	private Long bookPrice;
	
	@Column(name="book_view_count")
	private Long bookViewCount;
	
	@Column(name="book_wish_count")
	private Long bookWishCount;
	
	@Column(name="book_name")
	private String bookName;
	
	@Column(name="book_publisher_name")
	private String bookPublisherName;
	
	@Column(name="book_description")
	private String bookDescription;
	
	@Column(name="book_place")
	private String bookPlace;
	
	@Column(name="book_detailed_place")
	private String bookDetailedPlace;
	
	@Enumerated(EnumType.STRING)
	@Column(name="book_category")
	private BookCategory bookCategory;
	
	@Enumerated(EnumType.STRING)
	@Column(name="book_status")
	private BookStatus bookStatus;
	
	@Enumerated(EnumType.STRING)
	@Column(name="book_quality")
	private BookQuality bookQuality;
	
	@Column(name="book_create_time")
	private LocalDateTime bookCreateTime;
	
	@Column(name="book_suspend_time")
	private LocalDateTime bookSuspendTime;
	
	@Column(name="book_delete_time")
	private LocalDateTime bookDeleteTime;
	
	@OneToMany(mappedBy="book")
	private List<BookImageEntity> bookImages;
	
	@Column(name="member_id")
	private String memberID;
	
	public boolean isBookStatusNormal() {
		return bookStatus.equals(BookStatus.NORMAL);
	}
	
	public boolean isBookStatusEditable() {
		return bookStatus.equals(BookStatus.NORMAL) || bookStatus.equals(BookStatus.UNREGISTERED);
	}
	
	public void addBookImage(BookImageEntity bookImage) {
		if(this.bookImages.contains(bookImage)) {
			return;
		}
		
		if(bookImage.getBook()!=null) {
			bookImage.getBook().getBookImages().remove(bookImage);
		}
		
		this.bookImages.add(bookImage);
		bookImage.setBook(this);
	}
	
	public void deleteBookImage(BookImageEntity bookImage) {
		for(BookImageEntity item : this.bookImages) {
			if(item.equals(bookImage)&&(item.getBookImageStatus().equals(BookImageStatus.NORMAL)||item.getBookImageStatus().equals(BookImageStatus.UNREGISTERED))) {
				bookImage.setBook(null);
				bookImage.setBookImageDeleteTime(LocalDateTime.now());
				bookImage.setBookImageStatus(BookImageStatus.DELETED);
			}
		}
	}
	
	public Optional<BookImageEntity> getBookImage(Long bookImageID) {
		for(BookImageEntity bookImage : this.bookImages) {
			if(bookImage.equals(BookImageEntity.builder().bookImageID(bookImageID).build())) {
				return Optional.of(bookImage);
			}
		}
		
		return Optional.empty();
	}
	
	public void increaseBookViewCount() {
		this.bookViewCount++;
	}
	
	public void increaseBookWishCount() {
		this.bookWishCount++;
	}
	
	@Override
	public boolean equals(final Object o) {
	    if (this == o) {
	        return true;
	    }
	    
	    if (!(o instanceof BookEntity book)) {
	        return false;
	    }
	    
	    return Objects.equals(getBookID(), book.getBookID());
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(getBookID());
	}
}
