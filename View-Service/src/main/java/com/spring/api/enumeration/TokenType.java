package com.spring.api.enumeration;

import lombok.Getter;

@Getter
public enum TokenType {
	ACCESS_TOKEN("access-token",2L),
	REFRESH_TOKEN("refresh-token",336L);
	
	String tokenType;
	Long tokenTime;
	
	TokenType(String tokenType, Long tokenTime){
		this.tokenType = tokenType;
		this.tokenTime = tokenTime;
		
	}
}
