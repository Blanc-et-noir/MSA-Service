package com.spring.api.dto;

import java.util.List;
import java.util.Objects;

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
public class ReadMemberRequestByMemberIDDTO {
	@JsonProperty("member-id")
	private String memberID;
	@JsonProperty("member-statuses")
	private List<MemberStatus> memberStatuses;
	
	@Override
    public int hashCode() {
        return Objects.hash(memberID);
    }
}
