package com.spring.api.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import com.spring.api.enumeration.BookImageExtension;
import com.spring.api.enumeration.BookImageStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="book_image")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
	name = "book_image_seq_generator",
	sequenceName = "book_image_seq",
	initialValue = 1,
	allocationSize = 100
)
public class BookImageEntity {
	@Id
	@Column(name="book_image_id")
	@GeneratedValue(
		strategy = GenerationType.SEQUENCE,
		generator = "book_image_seq_generator"
	)
	private Long bookImageID;
	
	@Column(name="book_image_temporary_name")
	private String bookImageTemporaryName;
	
	@Enumerated(EnumType.STRING)
	@Column(name="book_image_extension")
	private BookImageExtension bookImageExtension;
	
	@Enumerated(EnumType.STRING)
	@Column(name="book_image_status")
	private BookImageStatus bookImageStatus;
	
	@Column(name="book_image_uri")
	private String bookImageURI;
	
	@Column(name="book_image_create_time")
	private LocalDateTime bookImageCreateTime;
	
	@Column(name="book_image_delete_time")
	private LocalDateTime bookImageDeleteTime;
	
	@Column(name="book_image_suspend_time")
	private LocalDateTime bookImageSuspendTime;
	
	@ManyToOne
	@JoinColumn(name="book_id", referencedColumnName="book_id")
	private BookEntity book;
	
	public void updateBook(BookEntity book) {
		if(this.book!=null) {
			for(BookImageEntity item : this.book.getBookImages()) {
				if(item.equals(this)&&item.getBookImageStatus().equals(BookImageStatus.NORMAL)) {
					item.setBook(null);
					item.setBookImageDeleteTime(LocalDateTime.now());
					item.setBookImageStatus(BookImageStatus.DELETED);
				}
			}
		}
		
		this.book = book;
		book.getBookImages().add(this);
	}
	
	public boolean isBookImageStatusEditable() {
		return bookImageStatus.equals(BookImageStatus.NORMAL) || bookImageStatus.equals(BookImageStatus.UNREGISTERED);
	}
	
	@Override
	public boolean equals(final Object o) {
	    if (this == o) {
	        return true;
	    }
	    
	    if (!(o instanceof BookImageEntity book)) {
	        return false;
	    }
	    
	    return Objects.equals(getBookImageID(), book.getBookImageID());
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(getBookImageID());
	}
	
}
