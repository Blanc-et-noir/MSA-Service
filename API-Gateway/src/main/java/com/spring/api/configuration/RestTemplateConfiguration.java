package com.spring.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.spring.api.handler.RestTemplateErrorHandler;

@Configuration
public class RestTemplateConfiguration {
	@Bean
    RestTemplate restTemplate(RestTemplateErrorHandler restTemplateErrorHandler) {
        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(5000);
        restTemplate.setRequestFactory(requestFactory);
        restTemplate.setErrorHandler(restTemplateErrorHandler);
        return restTemplate;
    }
}
