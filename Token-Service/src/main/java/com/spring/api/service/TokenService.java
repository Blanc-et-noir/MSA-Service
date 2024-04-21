package com.spring.api.service;

import com.spring.api.dto.CreateMemberTokensRequestDTO;
import com.spring.api.dto.CreateMemberTokensResponseDTO;
import com.spring.api.dto.DeleteMemberTokensRequestDTO;
import com.spring.api.dto.UpdateMemberTokensRequestDTO;
import com.spring.api.dto.UpdateMemberTokensResponseDTO;

public interface TokenService {
	public CreateMemberTokensResponseDTO createTokens(CreateMemberTokensRequestDTO dto);
	public UpdateMemberTokensResponseDTO updateTokens(UpdateMemberTokensRequestDTO dto);
	public void deleteTokens(DeleteMemberTokensRequestDTO dto);
}
