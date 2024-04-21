package com.spring.api.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MemberRole {
	ADMIN,
	GENERAL;
	
	@JsonCreator
    public static MemberRole from(String memberRole) {
        return MemberRole.valueOf(memberRole.toUpperCase());
    }
}
