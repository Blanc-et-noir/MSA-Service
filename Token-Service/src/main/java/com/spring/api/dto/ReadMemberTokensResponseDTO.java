package com.spring.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadMemberTokensResponseDTO {
	@JsonProperty("member-access-token")
	private TokenDTO memberAccessToken;
	
	@JsonProperty("member-refresh-token")
	private TokenDTO memberRefreshToken;
}
