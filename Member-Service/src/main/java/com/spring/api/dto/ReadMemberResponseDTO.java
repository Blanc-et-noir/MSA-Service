package com.spring.api.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.api.enumeration.MemberRole;

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
public class ReadMemberResponseDTO {
	@JsonProperty("member-id")
	private String memberID;
	@JsonProperty("member-pw")
	private String memberPW;
	@JsonProperty("member-role")
	private MemberRole memberRole;
	@JsonProperty("member-access-token")
	private String memberAccessToken;
	@JsonProperty("member-refresh-token")
	private String memberRefreshToken;
	@JsonProperty("member-email")
	private String memberEmail;
	
	@Override
	public boolean equals(Object obj) {
		if(this==obj) {
			return true;
		}
		
		if(!(obj instanceof ReadMemberResponseDTO)) {
			return false;
		}
		
		ReadMemberResponseDTO member = (ReadMemberResponseDTO) obj;
		
		return 
				this.memberID.equals(member.getMemberID())&&this.memberPW.equals(member.getMemberPW());
	}
	
	@Override
    public int hashCode() {
        return Objects.hash(memberID,memberPW);
    }
}
