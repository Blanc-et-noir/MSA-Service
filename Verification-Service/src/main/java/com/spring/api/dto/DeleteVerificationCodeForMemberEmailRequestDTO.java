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
public class DeleteVerificationCodeForMemberEmailRequestDTO {
	@JsonProperty("member-email")
	private String memberEmail;
	
	@JsonProperty("member-email-verification-code")
	private String memberEmailVerificationCode;
}
