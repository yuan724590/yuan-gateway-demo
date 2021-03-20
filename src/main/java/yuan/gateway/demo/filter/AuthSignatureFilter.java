package yuan.gateway.demo.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 调用鉴权,适用于所有请求
 */
@Component
@Slf4j
public class AuthSignatureFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("--- AuthSignatureFilter ---");
        String token = exchange.getRequest().getQueryParams().getFirst("authToken");
        if (null == token || token.isEmpty()) {
            //当请求不携带Token或者token为空时，直接设置请求状态码为401，返回(在页面上可看到效果)
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }
}
