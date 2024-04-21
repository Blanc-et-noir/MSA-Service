package com.spring.api.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.api.dto.CreateVerificationCodeForMemberEmailRequestDTO;
import com.spring.api.dto.CreateVerificationCodeForMemberPWRequestDTO;
import com.spring.api.dto.DeleteVerificationCodeForMemberEmailRequestDTO;
import com.spring.api.dto.DeleteVerificationCodeForMemberPWRequestDTO;
import com.spring.api.dto.ResponseDTO;
import com.spring.api.service.VerificationService;

@RestController
@RequestMapping("/api/v1/verifications")
public class VerficationController {
	private VerificationService verificationService;
	
	VerficationController(VerificationService verificationService){
		this.verificationService = verificationService;
	}
	
	@PostMapping("/member-emails")
	public ResponseDTO createVerificationCodeForMemberEmail(@RequestBody CreateVerificationCodeForMemberEmailRequestDTO dto) {
		verificationService.createVerificationCodeForMemberEmail(dto);
		return ResponseDTO.success("이메일 인증코드 발급 성공");
	}
	
	@DeleteMapping("/member-emails")
	public ResponseDTO deleteVerificationCodeForMemberEmail(@RequestBody DeleteVerificationCodeForMemberEmailRequestDTO dto) {
		verificationService.deleteVerificationCodeForMemberEmail(dto);
		return ResponseDTO.success("이메일 인증 성공");
	}
	
	@PostMapping("/member-pws")
	public ResponseDTO createVerificationCodeForMemberPW(@RequestBody CreateVerificationCodeForMemberPWRequestDTO dto) {
		verificationService.createVerificationCodeForMemberPW(dto);
		return ResponseDTO.success("이메일 인증코드 발급 성공");
	}
	
	@DeleteMapping("/member-pws")
	public ResponseDTO deleteVerificationCode(@RequestBody DeleteVerificationCodeForMemberPWRequestDTO dto) {
		verificationService.deleteVerificationCodeForMemberPW(dto);
		return ResponseDTO.success("이메일 인증 성공");
	}
}
