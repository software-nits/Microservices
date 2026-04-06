package com.software.apigatewayservice.filter;

import com.software.apigatewayservice.dto.ValidateTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    final RestTemplate restTemplate;

    public AuthenticationFilter(RestTemplate restTemplate) {
        super(Config.class);
        this.restTemplate = restTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        logger.info("all method call stack trace length: {}.", stackTrace.length);
        for (StackTraceElement stackTraceElement : stackTrace) {
            logger.info("all method call stack trace: {}.", stackTraceElement);
        }
        logger.info("started api gateway filter for security.");
        return ((exchange, chain) -> {
            logger.info("started checking url to filter for security.");
            Predicate<ServerHttpRequest> predicate = request -> Stream.of("authentication/register", "authentication/authenticate", "authentication/validate-token", "/actuator/**")
                    .noneMatch(uri -> {logger.info("request url is {}.",request.getURI().getPath());
                    return request.getURI().getPath().contains(uri);});
            if (predicate.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsHeader(HttpHeaders.AUTHORIZATION))
                    throw new RuntimeException("authorization header not found.");

                logger.info("checking auth header for security.");
                String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
//                if (authHeader != null && authHeader.startsWith(Constants.BEARER)) authHeader = authHeader.substring(7);
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", authHeader);
                HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
                logger.info("calling authentication service for token verification with header 'Authorization' {}.", authHeader);
                ValidateTokenResponse response = restTemplate.exchange("http://localhost:7091/authentication/validate-token", HttpMethod.GET, requestEntity, ValidateTokenResponse.class).getBody();
                // with this url authentication-service not working.
//                ValidateTokenResponse response = restTemplate.getForObject("http://localhost:7080/api/auth/user/get-token?token=" + authHeader, ValidateTokenResponse.class);
                logger.info("authentication completed with response {}.", response);
                assert response != null;
                if (!response.isValid()) throw new RuntimeException("not a valid token....!");
            }
            logger.info("completed api gateway filter for security.");
            return chain.filter(exchange);
        });
    }

    public static class Config { }
}
