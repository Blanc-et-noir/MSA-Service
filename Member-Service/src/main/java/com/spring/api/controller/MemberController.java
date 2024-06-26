package com.spring.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.api.dto.CreateMemberRequestDTO;
import com.spring.api.dto.MemberDTO;
import com.spring.api.dto.ReadMemberRequestByMemberEmailDTO;
import com.spring.api.dto.ReadMemberRequestByMemberIDDTO;
import com.spring.api.dto.ResponseDTO;
import com.spring.api.dto.SimpleMemberDTO;
import com.spring.api.dto.UpdateMemberPWRequestDTO;
import com.spring.api.dto.UpdateMemberRequestDTO;
import com.spring.api.dto.UpdateMemberTokensRequestDTO;
import com.spring.api.enumeration.MemberStatus;
import com.spring.api.service.MemberService;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {
	private MemberService memberService;
	
	MemberController(MemberService memberService){
		this.memberService = memberService;
	}
	
	@GetMapping("/{member-ids}")
	public ResponseDTO<MemberDTO> readMemberByMemberID(@PathVariable("member-ids") String memberID, @RequestParam(name="member-statuses",required=false) List<MemberStatus> memberStatuses) {
		return ResponseDTO.success("회원정보 조회 성공", memberService.readMemberByMemberID(
				ReadMemberRequestByMemberIDDTO.builder()
				.memberID(memberID)
				.memberStatuses(memberStatuses)
				.build()));
	}
	
	@GetMapping("/member-emails/{member-emails}")
	public ResponseDTO<SimpleMemberDTO> readMemberByMemberEmail(@PathVariable("member-emails") String memberEmail, @RequestParam(name="member-statuses",required=false) List<MemberStatus> memberStatuses) {
		return ResponseDTO.success("회원정보 조회 성공", memberService.readMemberByMemberEmail(
				ReadMemberRequestByMemberEmailDTO.builder()
				.memberEmail(memberEmail)
				.memberStatuses(memberStatuses)
				.build()));
	}
	
	//내부 서버용, 외부접근 차단
	@PutMapping("/{member-ids}/tokens")
	public ResponseDTO<?> updateMemberTokens(@PathVariable("member-ids") String memberID, @RequestBody UpdateMemberTokensRequestDTO dto) {
		memberService.updateMemberTokens(memberID,dto);
		return ResponseDTO.success("회원 토큰정보 변경 성공");
	}
	
	//내부 서버용, 외부접근 차단
	@PatchMapping("/{member-ids}/member-pws")
	public ResponseDTO<?> updateMemberPW(@PathVariable("member-ids") String memberID, @RequestBody UpdateMemberPWRequestDTO dto) {
		return ResponseDTO.success("회원 PW 변경 성공",memberService.updateMemberPW(memberID, dto));
	}
	
	@PatchMapping("/{member-ids}")
	public ResponseDTO<?> updateMember(@RequestHeader("Member-id") String memberID1, @PathVariable("member-ids") String memberID2, @RequestBody UpdateMemberRequestDTO dto) {
		memberService.updateMember(memberID1, memberID2, dto);
		return ResponseDTO.success("회원 PW 변경 성공");
	}
	
	@PostMapping("")
	public ResponseDTO<?> createMember(@RequestBody CreateMemberRequestDTO dto) {
		memberService.createMember(dto);
		return ResponseDTO.success("회원정보 등록 성공");
	}
}
