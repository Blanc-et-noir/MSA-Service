package com.spring.api.filter;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Flux;

public class CachedRequestBody extends ServerHttpRequestDecorator {
    private final byte[] bytes;
    private final ServerWebExchange exchange;

    public CachedRequestBody(ServerWebExchange exchange, byte[] bytes) {
        super(exchange.getRequest());
        this.bytes = bytes;
        this.exchange = exchange;
    }

    @Override
    public Flux<DataBuffer> getBody() {
        return bytes==null||bytes.length==0?Flux.empty():Flux.just(exchange.getResponse().bufferFactory().wrap(bytes));
    }
}