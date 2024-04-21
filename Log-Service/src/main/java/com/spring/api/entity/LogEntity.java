package com.spring.api.entity;

import java.time.LocalDateTime;

import com.spring.api.enumeration.LogResponseStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="log")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
	name = "log_seq_generator",
	sequenceName = "log_seq",
	initialValue = 1,
	allocationSize = 100
)
public class LogEntity {
	@Id
	@Column(name="log_id")
	@GeneratedValue(
		strategy = GenerationType.SEQUENCE,
		generator = "log_seq_generator"
	)
	private Long logID;
	@Column(name="log_method")
	private String logMethod;
	@Column(name="log_uri")
	private String logURI;
	@Column(name="log_content_type")
	private String logContentType;
	@Column(name="log_parameter")
	private String logParameter;
	@Column(name="log_member_id")
	private String logMemberID;
	@Column(name="log_member_ip")
	private String logMemberIP;
	@Column(name="log_member_port")
	private String logMemberPort;
	@Column(name="log_request_time")
	private LocalDateTime logRequestTime;
	@Enumerated(EnumType.STRING)
	@Column(name="log_response_status")
	private LogResponseStatus logResponseStatus;
	@Column(name="log_error_code")
	private String logErrorCode;
}
