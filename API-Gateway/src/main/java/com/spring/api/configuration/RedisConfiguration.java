package com.spring.api.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {
	private final String host;
    private final String password;
    private final int port;
    
    RedisConfiguration(
    	@Value("${spring.data.redis.host}") String host, 
    	@Value("${spring.data.redis.password}") String password, 
    	@Value("${spring.data.redis.port}") int port
    ){
    	this.host = host;
    	this.port = port;
    	this.password = password;
    }
    
    @Bean
    RedisConnectionFactory redisConnectionFactory() {
    	RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
    	redisStandaloneConfiguration.setHostName(host);
    	redisStandaloneConfiguration.setPort(port);
    	redisStandaloneConfiguration.setPassword(password);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean("redis")
    RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
}
