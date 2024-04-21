package com.spring.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.api.dto.CreateLogRequestDTO;
import com.spring.api.dto.ResponseDTO;
import com.spring.api.service.LogService;

@RestController
@RequestMapping("/api/v1/logs")
public class LogController {
	private LogService logService;
	
	LogController(LogService logService){
		this.logService = logService;
	}
	
	@PostMapping("")
	public ResponseDTO createLog(@RequestBody CreateLogRequestDTO dto) {
		logService.createLog(dto);
		return ResponseDTO.success("로그 등록 성공");
	}
}
