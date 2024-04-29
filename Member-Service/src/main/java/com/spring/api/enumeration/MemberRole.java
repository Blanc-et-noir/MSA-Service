package com.spring.api.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.spring.api.code.MemberServiceCode;
import com.spring.api.exception.CustomException;

public enum MemberRole {
	ADMIN,
	GENERAL;
	
	@JsonCreator
    public static MemberRole from(String memberRole) {
		try {
			return MemberRole.valueOf(memberRole.toUpperCase());
		}catch(Exception e) {
			throw new CustomException(MemberServiceCode.MEMBER_ROLE_NOT_AVAILABLE);
		}
        
    }
}
