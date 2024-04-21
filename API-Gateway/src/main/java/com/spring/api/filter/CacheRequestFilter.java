package com.spring.api.filter;

import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

public class CacheRequestFilter implements WebFilter {
	private static final byte[] EMPTY_BYTES = new byte[0];
	
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		return DataBufferUtils
        .join(exchange.getRequest().getBody())
        .map(databuffer -> {
            final byte[] bytes = new byte[databuffer.readableByteCount()];
            DataBufferUtils.release(databuffer.read(bytes));

            return bytes;
        })
        .defaultIfEmpty(EMPTY_BYTES)
        .flatMap(bytes -> {
            final CachedRequestBody decorator = new CachedRequestBody(exchange, bytes);
            return chain.filter(exchange.mutate().request(decorator).build());
        });
    }
}
