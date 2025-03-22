package com.software.apigatewayservice.constants;

import java.util.stream.Stream;

public interface Constants {
    String BEARER = "Bearer ";
    String VALIDATE_TOKEN = "http://localhost:8060/api/auth/user/get-token?token=";
    Stream<String> URI = Stream.of("api/auth/user/register", "api/auth/user/authenticate");
}
