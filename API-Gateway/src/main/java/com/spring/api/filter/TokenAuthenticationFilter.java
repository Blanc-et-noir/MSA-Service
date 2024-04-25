package com.spring.api.filter;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.IncorrectClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.MissingClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.api.code.Code;
import com.spring.api.code.ErrorCode;
import com.spring.api.dto.ResponseDTO;
import com.spring.api.enumeration.TokenType;
import com.spring.api.util.RedisUtil;
import com.spring.api.util.TokenUtil;

import reactor.core.publisher.Mono;

public class TokenAuthenticationFilter implements WebFilter {
	private RedisUtil redisUtil;
	private TokenUtil tokenUtil;
	
	public TokenAuthenticationFilter(RedisUtil redisUtil,TokenUtil tokenUtil) {
		this.redisUtil = redisUtil;
		this.tokenUtil = tokenUtil;
	}
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain ch) {
		ServerHttpRequest request = exchange.getRequest();
		ServerHttpResponse response = exchange.getResponse();
		List<String> list = request.getHeaders().get(TokenType.MEMBER_ACCESS_TOKEN.getTokenType());
		String memberAccessToken = list!=null&&!list.isEmpty()?list.getFirst():null;
		
		try {
			if(memberAccessToken==null) {
				return onError(exchange, response, ErrorCode.TOKEN_FOR_ACCESS_NOT_FOUND);
			}
			
			if(tokenUtil.isExpiredWithDecoding(memberAccessToken)) {
				return onError(exchange, response, ErrorCode.TOKEN_FOR_ACCESS_EXPIRED);
			}
			
			if(!tokenUtil.isAccessTokenWithDecoding(memberAccessToken)) {
				return onError(exchange, response, ErrorCode.TOKEN_NOT_FOR_ACCESS);
			}
			
			if(redisUtil.getString(memberAccessToken)!=null) {
				return onError(exchange, response, ErrorCode.TOKEN_FOR_ACCESS_INVALIDATED);
			}
			
			tokenUtil.verify(memberAccessToken);

		}catch(AlgorithmMismatchException e) {
			return onError(exchange, response, ErrorCode.TOKEN_FOR_ACCESS_FORGED);
		}catch(SignatureVerificationException e) {
			return onError(exchange, response, ErrorCode.TOKEN_FOR_ACCESS_FORGED);
		}catch(TokenExpiredException e) {
			return onError(exchange, response, ErrorCode.TOKEN_FOR_ACCESS_EXPIRED);
		}catch(MissingClaimException e) {
			return onError(exchange, response, ErrorCode.TOKEN_FOR_ACCESS_FORGED);
		}catch(IncorrectClaimException e) {
			return onError(exchange, response, ErrorCode.TOKEN_FOR_ACCESS_FORGED);
		}catch(JWTVerificationException e) {
			return onError(exchange, response, ErrorCode.TOKEN_FOR_ACCESS_FORGED);
		}catch(Exception e) {
			return onError(exchange, response, ErrorCode.INTERNAL_SERVER_ERROR);
		}
		
		exchange.getRequest().mutate().headers(h -> h.add("Member-id",tokenUtil.getMemberIDWithDecoding(memberAccessToken))).build();
		
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				tokenUtil.getMemberIDWithDecoding(memberAccessToken),
				null,
				List.<GrantedAuthority>of(new SimpleGrantedAuthority(tokenUtil.getMemberRoleWithDecoding(memberAccessToken))));
		
        return ch
        		.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
	}
	
	private Mono<Void> onError(ServerWebExchange exchange, ServerHttpResponse response, Code code){		
		ResponseDTO dto = ResponseDTO.fail(code);
		response.setStatusCode(code.getStatus());
		response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
		
		try {
		    DataBuffer buffer = response.bufferFactory().wrap(new ObjectMapper().writeValueAsString(dto).getBytes(StandardCharsets.UTF_8));
		    return response.writeWith(Mono.just(buffer));
		}catch(Exception e) {
			DataBuffer buffer = response.bufferFactory().wrap(ErrorCode.INTERNAL_SERVER_ERROR.getCode().getBytes(StandardCharsets.UTF_8));
		    return response.writeWith(Mono.just(buffer));
		}
	}
}
