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
public class CreateVerificationCodeForMemberPWRequestDTO {
	@JsonProperty("member-id")
	private String memberID;
	
	@JsonProperty("member-email")
	private String memberEmail;
}
