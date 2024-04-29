package com.spring.api.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.spring.api.code.MemberServiceCode;
import com.spring.api.exception.CustomException;

public enum MemberStatus {
	NORMAL,
	DELETED,
	SUSPENDED;
	
	@JsonCreator
    public static MemberStatus from(String memberRole) {
		try {
			return MemberStatus.valueOf(memberRole.toUpperCase());
		}catch(Exception e) {
			throw new CustomException(MemberServiceCode.MEMBER_STATUS_NOT_AVAILABLE);
		}
    }
}
