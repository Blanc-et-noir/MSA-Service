package com.spring.api.code;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum BookServiceCode implements Code{
	BOOK_NOT_FOUND("BOOK_NOT_FOUND","해당 도서 정보가 존재하지 않습니다.",HttpStatus.BAD_REQUEST),
	BOOK_NOT_EDITABLE_DUE_TO_BOOK_STATUS("BOOK_NOT_EDITABLE_DUE_TO_BOOK_STATUS","해당 도서는 현재 수정할 수 있는 상태가 아닙니다.",HttpStatus.BAD_REQUEST),
	BOOK_NOT_EDITABLE_DUE_TO_MEMBER_ID("BOOK_NOT_EDITABLE_DUE_TO_MEMBER_ID","해당 도서는 자신이 등록한 도서가 아니므로 수정할 수 없습니다.",HttpStatus.BAD_REQUEST),
	BOOK_IMAGE_NOT_FOUND("BOOK_IMAGE_NOT_FOUND","해당 도서 이미지 정보가 존재하지 않습니다.",HttpStatus.BAD_REQUEST),
	BOOK_IMAGE_NOT_EDITABLE_DUE_TO_BOOK_IMAGE_STATUS("BOOK_IMAGE_NOT_EDITABLE_DUE_TO_BOOK_IMAGE_STATUS","해당 도서 이미지는 수정할 수 있는 상태가 아닙니다.",HttpStatus.BAD_REQUEST),
	BOOK_IMAGE_NOT_INCLUDED_IN_BOOK("BOOK_IMAGE_NOT_INCLUDED_IN_BOOK","해당 도서 이미지는 해당 도서에 포함된 이미지가 아닙니다.",HttpStatus.BAD_REQUEST),
	BOOK_IMAGE_NOT_UPLOADED_DUE_TO_ERROR("BOOK_IMAGE_NOT_UPLOADED_DUE_TO_ERROR","해당 도서 이미지를 업로드 하는 과정에서 에러가 발생했습니다.",HttpStatus.BAD_REQUEST),
	BOOK_IMAGE_EXTENSION_NOT_AVAILABLE("BOOK_IMAGE_EXTENSION_NOT_AVAILABLE","해당 도서 이미지 확장자는 사용하실 수 없습니다.",HttpStatus.BAD_REQUEST),
	BOOK_STATUS_NOT_AVAILABLE("BOOK_STATUS_NOT_AVAILABLE","해당 도서 상태는 사용하실 수 없습니다.",HttpStatus.BAD_REQUEST),
	BOOK_QUALITY_NOT_AVAILABLE("BOOK_QUALITY_NOT_AVAILABLE","해당 도서 품질은 사용하실 수 없습니다.",HttpStatus.BAD_REQUEST),
	BOOK_IMAGE_STATUS_NOT_AVAILABLE("BOOK_IMAGE_STATUS_NOT_AVAILABLE","해당 도서 이미지 상태는 사용하실 수 없습니다.",HttpStatus.BAD_REQUEST),
	BOOK_CATEGORY_NOT_AVAILABLE("BOOK_CATEGORY_NOT_AVAILABLE","해당 도서 카테고리는 사용하실 수 없습니다.",HttpStatus.BAD_REQUEST);
	
	private String code;
	private String message;
	private HttpStatus status; 
	
	BookServiceCode(String code, String message, HttpStatus status){
		this.code = code;
		this.message = message;
		this.status = status;
	}
}
