package com.spring.api.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.api.dto.CreateMemberTokensRequestDTO;
import com.spring.api.dto.DeleteMemberTokensRequestDTO;
import com.spring.api.dto.ResponseDTO;
import com.spring.api.dto.UpdateMemberTokensRequestDTO;
import com.spring.api.service.TokenService;

@RestController
@RequestMapping("/api/v1/tokens")
public class TokenController {
	private TokenService tokenService;
	
	TokenController(TokenService tokenService){
		this.tokenService = tokenService;
	}
	
	@PostMapping("")
	public ResponseDTO createTokens(@RequestBody CreateMemberTokensRequestDTO dto){
		return ResponseDTO.success("토큰 발급 성공",tokenService.createTokens(dto));
	}
	
	@PutMapping("")
	public ResponseDTO updateTokens(@RequestBody UpdateMemberTokensRequestDTO dto){
		;
		return ResponseDTO.success("토큰 갱신 성공",tokenService.updateTokens(dto));
	}
	
	@DeleteMapping("")
	public ResponseDTO deleteTokens(@RequestBody DeleteMemberTokensRequestDTO dto){
		tokenService.deleteTokens(dto);
		return ResponseDTO.success("토큰 폐지 성공");
	}
}
