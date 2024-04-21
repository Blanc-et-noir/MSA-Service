package com.spring.api.service;

import org.springframework.stereotype.Service;

import com.spring.api.dto.CreateLogRequestDTO;
import com.spring.api.entity.LogEntity;
import com.spring.api.repository.LogRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service("logService")
public class LogServiceImpl implements LogService{
	private LogRepository logRepository;
	
	LogServiceImpl(LogRepository logRepository){
		this.logRepository = logRepository;
	}

	@Override
	public void createLog(CreateLogRequestDTO dto) {
		LogEntity log = LogEntity.builder()
			.logContentType(dto.getLogContentType())
			.logErrorCode(dto.getLogErrorCode())
			.logMemberID(dto.getLogMemberID())
			.logMemberIP(dto.getLogMemberIP())
			.logMemberPort(dto.getLogMemberPort())
			.logMethod(dto.getLogMethod())
			.logParameter(dto.getLogParameter())
			.logRequestTime(dto.getLogRequestTime())
			.logResponseStatus(dto.getLogResponseStatus())
			.logURI(dto.getLogURI())
			.build();
		
		logRepository.save(log);
	}
}
