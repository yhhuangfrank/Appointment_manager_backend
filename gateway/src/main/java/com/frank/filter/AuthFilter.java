package com.frank.filter;

import com.frank.common.JwtHelper;
import com.frank.common.Result;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class AuthFilter implements GlobalFilter, Ordered {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        System.out.printf("path: %s%n", path);

        String userId = this.getUserId(request);
        if (antPathMatcher.match("/api/**/auth/**", path) && StringUtils.isEmpty(userId)) {
            ServerHttpResponse response = exchange.getResponse();
            return reject(response);
        }
        return chain.filter(exchange);
    }

    private String getUserId(ServerHttpRequest request) {
        String token = "";
        List<String> list = request.getHeaders().get("token");
        if (!CollectionUtils.isEmpty(list)) {
            token = list.get(0);
        }
        if (StringUtils.isNotBlank(token)) {
            return String.valueOf(JwtHelper.getUserId(token));
        }
        return null;
    }

    private Mono<Void> reject(ServerHttpResponse response) {
        Result<?> result = Result.fail("Unauthenticated!");
        Gson gson = new Gson();
        byte[] bytes = gson.toJson(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(dataBuffer));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
