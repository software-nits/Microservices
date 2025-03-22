package com.software.authenticationservice.service;

import com.software.authenticationservice.bean.User;
import com.software.authenticationservice.dto.AuthenticationRequest;
import com.software.authenticationservice.dto.AuthenticationResponse;
import com.software.authenticationservice.dto.RegisterRequest;
import com.software.authenticationservice.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        User user = new User();
        BeanUtils.copyProperties(registerRequest, user);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(user);
        return getAuthenticationResponse(jwtService.generateToken(user));
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),authenticationRequest.getPassword()));
        User user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow();
        return getAuthenticationResponse(jwtService.generateToken(user));
    }

    private AuthenticationResponse getAuthenticationResponse(String token) {
        return new AuthenticationResponse(token);
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElse(new User());
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}
