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
public class UpdateMemberRequestDTO {
	@JsonProperty(value="member-pw")
	private String memberPW;
	@JsonProperty(value="member-pw-check")
	private String memberPWCheck;
	@JsonProperty(value="member-email")
	private String memberEmail;
	@JsonProperty(value="member-name")
	private String memberName;
}
