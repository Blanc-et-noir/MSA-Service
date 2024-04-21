package com.spring.api.filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.spring.api.dto.CreateLogRequestDTO;
import com.spring.api.enumeration.LogResponseStatus;
import com.spring.api.enumeration.TokenType;
import com.spring.api.util.TokenUtil;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class LogFilter implements WebFilter {
	private RestTemplate restTemplate;
	private TokenUtil tokenUtil;
	
	public LogFilter(RestTemplate restTemplate, TokenUtil tokenUtil) {
		this.restTemplate = restTemplate;
		this.tokenUtil = tokenUtil;
	}

	@Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		return chain.filter(exchange)
            .doOnEach(signal -> {
                if(signal.isOnComplete()||signal.isOnError()) {
                    createLog(createLogDTO(exchange));
                }
            });
    }
	
	private void createLog(CreateLogRequestDTO dto) {
		try {
			restTemplate.postForEntity("http://localhost:3008/api/v1/logs", dto, CreateLogRequestDTO.class);
		}catch(Exception e) {
			
		}
	}
	
	private InputStream fluxToInputStream(Flux<DataBuffer> data) {
		try {
			PipedOutputStream osPipe = new PipedOutputStream();
		    PipedInputStream isPipe = new PipedInputStream(osPipe);

		    DataBufferUtils.write(data, osPipe)
		            .subscribeOn(Schedulers.boundedElastic())
		            .doOnComplete(() -> {
		                try {
		                    osPipe.close();
		                } catch (IOException ignored) {
		                }
		            })
		            .subscribe(DataBufferUtils.releaseConsumer());
		    return isPipe;
		}catch(Exception e) {
			return null;
		}
	}
	
	private CreateLogRequestDTO createLogDTO(ServerWebExchange ex) {
		ServerHttpRequest request = ex.getRequest();
		ServerHttpResponse response = ex.getResponse();
		
    	List<HttpCookie> cookies = request
    		.getCookies()
    		.getOrDefault(TokenType.MEMBER_ACCESS_TOKEN.getTokenType(), new LinkedList<HttpCookie>());
    	InetSocketAddress isa = request.getRemoteAddress();
    	MediaType contentType = request.getHeaders().getContentType();
    	HttpMethod logMethod = request.getMethod();
    	LocalDateTime logRequestTime = LocalDateTime.now();
    	
    	String logMemberIP = isa!=null?isa.getAddress().toString():null;
    	String logMemberPort = isa!=null?isa.getPort()+"":null;
    	String memberAccessToken = cookies.isEmpty()?null:cookies.getFirst().getValue();
    	String memberID = memberAccessToken!=null?tokenUtil.getMemberIDWithDecoding(memberAccessToken):null;
    	String logParameter = null;
    	
    	if(contentType==null||logMethod.equals(HttpMethod.GET)) {
    		MultiValueMap<String,String> params = request.getQueryParams();
    		if(!params.isEmpty()) {
    			JSONObject json = new JSONObject();
        		for(String key : params.keySet()) {
        			JSONArray array = new JSONArray();
        			for(String value : params.get(key)) {
        				array.put(value);
        			}
        			json.put(key, array);
        		}
        		logParameter = json.toString();
    		}
    	}else if(contentType.equals(MediaType.APPLICATION_JSON)) {
    		try {
    			InputStream inputStream = fluxToInputStream(request.getBody());
                logParameter = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name()).replaceAll(" ", "");
    		}catch(Exception e) {
    			logParameter = null;
    		}
    	}
        
        CreateLogRequestDTO dto = CreateLogRequestDTO.builder()
    			.logMethod(logMethod.name())
    			.logURI(request.getURI().getPath())
    			.logContentType(contentType!=null?contentType.toString():null)
    			.logMemberID(memberID)
    			.logMemberIP(logMemberIP)
    			.logMemberPort(logMemberPort)
    			.logParameter(logParameter)
    			.logRequestTime(logRequestTime)
    			.logResponseStatus(!response.getStatusCode().isError()?LogResponseStatus.SUCCESS:LogResponseStatus.FAIL)
    			//.logErrorCode()
    			.build();
        return dto;
	}
	
}
