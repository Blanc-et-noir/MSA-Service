package com.spring.api.entity;

import java.time.LocalDateTime;

import com.spring.api.enumeration.MemberRole;
import com.spring.api.enumeration.MemberStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table(name="member")
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity {
	@Id
	@Column(name="member_id")
	private String memberID;
	
	@Column(name="member_name")
	private String memberName;
	
	@Column(name="member_pw")
	private String memberPW;
	
	@Column(name="member_email")
	private String memberEmail;
	
	@Enumerated(EnumType.STRING)
	@Column(name="member_role")
	private MemberRole memberRole;
	
	@Enumerated(EnumType.STRING)
	@Column(name="member_status")
	private MemberStatus memberStatus;
	
	@Column(name="member_access_token")
	private String memberAccessToken;
	
	@Column(name="member_refresh_token")
	private String memberRefreshToken;
	
	@Column(name="member_join_time")
	private LocalDateTime memberJoinTime;
	
	@Column(name="member_suspend_time")
	private LocalDateTime memberSuspendTime;
	
	@Column(name="member_delete_time")
	private LocalDateTime memberDeleteTime;
	
	public boolean isMemberStatusNormal() {
		return this.memberStatus.equals(MemberStatus.NORMAL);
	}
	
	public void updateTokens(String memberAccessToken, String memberRefreshToken) {
		this.memberAccessToken = memberAccessToken;
		this.memberRefreshToken = memberRefreshToken;
	}
	
	public void updatePw(String memberPW) {
		this.memberPW = memberPW;
	}
}
