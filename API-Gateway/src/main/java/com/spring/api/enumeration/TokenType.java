package com.spring.api.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.spring.api.code.TokenServiceCode;
import com.spring.api.exception.CustomException;

import lombok.Getter;

@Getter
public enum TokenType {
	MEMBER_ACCESS_TOKEN("member-access-token",2L),
	MEMBER_REFRESH_TOKEN("member-refresh-token",336L);
	
	String tokenType;
	Long tokenTime;
	
	TokenType(String tokenType, Long tokenTime){
		this.tokenType = tokenType;
		this.tokenTime = tokenTime;
		
	}
	
	@JsonCreator
    public static TokenType from(String tokenType) {
		try {
        	return TokenType.valueOf(tokenType.toUpperCase());
        }catch(Exception e) {
        	throw new CustomException(TokenServiceCode.TOKEN_TYPE_NOT_AVAILABLE);
        }
    }
}
