package com.spring.api.util;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component("redisUtil")
public class RedisUtil {
	private RedisTemplate redisTemplate;
	
	RedisUtil(@Qualifier("redis") RedisTemplate redisTemplate){
		this.redisTemplate = redisTemplate;
	}
	
	public String getString(String name) {
		return (String) redisTemplate.opsForValue().get(name);
	}
	
	public void setString(String name, String value, Long miliSeconds) {
		if(name!=null&&value!=null&&miliSeconds>0) {
			redisTemplate.opsForValue().set(name, value, miliSeconds, TimeUnit.MILLISECONDS);
		}
		return;
	}
	
	public void delete(String name) {
		redisTemplate.delete(name);
	}
}
