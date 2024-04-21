package com.spring.api.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;

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
    public static TokenType from(String s) {
        if(MEMBER_ACCESS_TOKEN.getTokenType().equals(s)) {
        	return MEMBER_ACCESS_TOKEN;
        }else if(MEMBER_REFRESH_TOKEN.getTokenType().equals(s)) {
        	return MEMBER_REFRESH_TOKEN;
        }else {
        	return null;
        }
    }
}
