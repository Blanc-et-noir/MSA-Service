package com.spring.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.api.entity.LogEntity;

@Repository("logRepository")
public interface LogRepository extends JpaRepository<LogEntity, Long>{
	
}
