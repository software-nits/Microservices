package com.software.authenticationservice.controller;

import com.software.authenticationservice.dto.AuthenticationRequest;
import com.software.authenticationservice.dto.AuthenticationResponse;
import com.software.authenticationservice.dto.RegisterRequest;
import com.software.authenticationservice.dto.ValidateTokenResponse;
import com.software.authenticationservice.service.JwtService;
import com.software.authenticationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private UserService userService;
    @Autowired
    private  JwtService jwtService;

    @ResponseStatus(HttpStatus.CREATED) // 201
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(userService.register(registerRequest));
    }

    @ResponseStatus(HttpStatus.ACCEPTED) // 201
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(userService.authenticate(authenticationRequest));
    }

    @GetMapping("/validate-token")
    public ResponseEntity<ValidateTokenResponse> validateToken(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(jwtService.validateToken(token));
    }
}
