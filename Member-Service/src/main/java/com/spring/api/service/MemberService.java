package com.spring.api.service;

import com.spring.api.dto.CreateMemberRequestDTO;
import com.spring.api.dto.MemberDTO;
import com.spring.api.dto.SimpleMemberDTO;
import com.spring.api.dto.UpdateMemberPWRequestDTO;
import com.spring.api.dto.UpdateMemberTokensRequestDTO;

public interface MemberService {
	public void createMember(CreateMemberRequestDTO dto);
	public void updateMemberTokens(String memberID, UpdateMemberTokensRequestDTO dto);
	public MemberDTO readMemberByMemberID(String memberID);
	public MemberDTO updateMemberPW(String memberID, UpdateMemberPWRequestDTO dto);
	public SimpleMemberDTO readMemberByMemberEmail(String memberEmail);
}
