package com.spring.api.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.api.enumeration.LogResponseStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter	
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLogRequestDTO {	
	@JsonProperty("log-method")
	private String logMethod;
	
	@JsonProperty("log-uri")
	private String logURI;
	
	@JsonProperty("log-content-type")
	private String logContentType;
	
	@JsonProperty("log-parameter")
	private String logParameter;
	
	@JsonProperty("log-member-id")
	private String logMemberID;
	
	@JsonProperty("log-member-ip")
	private String logMemberIP;
	
	@JsonProperty("log-member-port")
	private String logMemberPort;
	
	@JsonProperty("log-request-time")
	private LocalDateTime logRequestTime;
	
	@JsonProperty("log-response-status")
	private LogResponseStatus logResponseStatus;
	
	@JsonProperty("log-error-code")
	private String logErrorCode;
}
