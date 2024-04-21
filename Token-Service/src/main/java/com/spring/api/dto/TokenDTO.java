package com.spring.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.api.enumeration.MemberRole;
import com.spring.api.enumeration.TokenType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class TokenDTO {
	@JsonProperty("token-type")
	private TokenType tokenType;
	
	@JsonProperty("token-remaining-time")
	private long tokenRemainingTime;
	
	@JsonProperty("member-id")
	private String memberID;
	
	@JsonProperty("member-role")
	private MemberRole memberRole;
}
