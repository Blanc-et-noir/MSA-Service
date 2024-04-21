package com.spring.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class UpdateMemberTokensResponseDTO {
	@JsonProperty("member-access-token")
	private String memberAccessToken;
	
	@JsonProperty("member-refresh-token")
	private String memberRefreshToken;
}
