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
    	
    }
}
