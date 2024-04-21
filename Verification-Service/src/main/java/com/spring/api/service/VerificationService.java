package com.spring.api.service;

import com.spring.api.dto.CreateVerificationCodeForMemberEmailRequestDTO;
import com.spring.api.dto.CreateVerificationCodeForMemberPWRequestDTO;
import com.spring.api.dto.DeleteVerificationCodeForMemberEmailRequestDTO;
import com.spring.api.dto.DeleteVerificationCodeForMemberPWRequestDTO;

public interface VerificationService {
	public void createVerificationCodeForMemberEmail(CreateVerificationCodeForMemberEmailRequestDTO dto);
	public void deleteVerificationCodeForMemberEmail(DeleteVerificationCodeForMemberEmailRequestDTO dto);
	public void deleteVerificationCodeForMemberPW(DeleteVerificationCodeForMemberPWRequestDTO dto);
	public void createVerificationCodeForMemberPW(CreateVerificationCodeForMemberPWRequestDTO dto);
}
