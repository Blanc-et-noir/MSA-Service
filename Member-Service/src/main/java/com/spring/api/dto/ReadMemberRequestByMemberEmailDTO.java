package com.spring.api.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.api.enumeration.MemberStatus;

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
public class ReadMemberRequestByMemberEmailDTO {
	@JsonProperty("member-email")
	private String memberEmail;
	@JsonProperty("member-statuses")
	private List<MemberStatus> memberStatuses;
}
