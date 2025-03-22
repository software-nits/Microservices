package com.software.apigatewayservice.filter;

import com.software.apigatewayservice.constants.Constants;
import com.software.apigatewayservice.dto.ValidateTokenResponse;
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

    final RestTemplate restTemplate;

    public AuthenticationFilter(RestTemplate restTemplate) {
        super(Config.class);
        this.restTemplate = restTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            Predicate<ServerHttpRequest> predicate = request -> Stream.of("authentication/register", "authentication/authenticate", "authentication/validate-token", "/actuator/**").noneMatch(uri -> request.getURI().getPath().contains(uri));
            if (predicate.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                    throw new RuntimeException("authorization header not found.");

                String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
//                if (authHeader != null && authHeader.startsWith(Constants.BEARER)) authHeader = authHeader.substring(7);
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", authHeader);
                HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
                ValidateTokenResponse response = restTemplate.exchange("http://localhost:7090/authentication/validate-token", HttpMethod.GET, requestEntity, ValidateTokenResponse.class).getBody();
//                ValidateTokenResponse response = restTemplate.getForObject("http://localhost:7080/api/auth/user/get-token?token=" + authHeader, ValidateTokenResponse.class);
                assert response != null;
                if (!response.isValid()) throw new RuntimeException("not a valid token....!");
            }
            return chain.filter(exchange);
        });
    }

    public static class Config { }
}
