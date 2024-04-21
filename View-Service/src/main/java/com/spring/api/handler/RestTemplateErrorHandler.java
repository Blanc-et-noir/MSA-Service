package com.spring.api.handler;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

@Component
public class RestTemplateErrorHandler extends DefaultResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
    	return super.hasError(response);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
    	System.out.println(response.getStatusCode().value());
    	if(response.getStatusCode().is4xxClientError()) {
    		String message = new String(response.getBody().readAllBytes());
    		System.out.println(message);
    	}else if(response.getStatusCode().is5xxServerError()) {
    		String message = new String(response.getBody().readAllBytes());
    		System.out.println(message);
    	}
    }
}
