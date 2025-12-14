package com.zjsu.lyy.gateway.filter;

import com.zjsu.lyy.gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    private static final List<String> WHITE_LIST = List.of(
            "/api/auth/login",
            "/api/auth/register"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        System.out.println(">>> JWT Filter path = " + path);

        // 白名单
        if (WHITE_LIST.stream().anyMatch(path::startsWith)) {
            System.out.println(">>> 白名单放行");
            return chain.filter(exchange);
        }

        // 拿 Token
        String header = exchange.getRequest().getHeaders().getFirst("Authorization");
        System.out.println(">>> Authorization header = " + header);

        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            System.out.println(">>> 无token或格式错误，返回401");
            return unauthorized(exchange);
        }
        String token = header.substring(7);
        System.out.println(">>> 提取token = " + token);

        // 验证
        if (!jwtUtil.validateToken(token)) {
            System.out.println(">>> token无效，返回401");
            return unauthorized(exchange);
        }

        // 解析并加头
        Claims claims = jwtUtil.parseToken(token);
        String uid = claims.getSubject();
        String uname = claims.get("username", String.class);
        System.out.println(">>> 网关加头：X-User-Id = " + uid + ", X-Username = " + uname);

        ServerHttpRequest mutated = exchange.getRequest().mutate()
                .header("X-User-Id", uid)
                .header("X-Username", uname)
                .header("X-User-Role", claims.get("role", String.class))
                .build();
        return chain.filter(exchange.mutate().request(mutated).build());
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        ServerHttpResponse resp = exchange.getResponse();
        resp.setStatusCode(HttpStatus.UNAUTHORIZED);
        return resp.setComplete();
    }

    @Override
    public int getOrder() {
        return -100;   // 越早越好
    }
}