package com.spring.api.dto.response;

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
public class MemberDTO {
	@JsonProperty("member-id")
	private String memberID;
	@JsonProperty("member-pw")
	private String memberPW;
	@JsonProperty("member-email")
	private String memberEmail;
	@JsonProperty("member-role")
	private MemberRole memberRole;
	@JsonProperty("member-access-token")
	private String memberAccessToken;
	@JsonProperty("member-refresh-token")
	private String memberRefreshToken;
	
	@Override
	public boolean equals(Object obj) {
		if(this==obj) {
			return true;
		}
		
		if(!(obj instanceof MemberDTO)) {
			return false;
		}
		
		MemberDTO member = (MemberDTO) obj;
		
		return 
				this.memberID.equals(member.getMemberID())&&
				this.memberPW.equals(member.getMemberPW());
	}
	
	@Override
    public int hashCode() {
        return Objects.hash(memberID,memberPW);
    }
}
