package com.spring.api.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MemberStatus {
	NORMAL,
	DELETED,
	SUSPENDED;
	
	@JsonCreator
    public static MemberStatus from(String memberRole) {
        return MemberStatus.valueOf(memberRole.toUpperCase());
    }
}
