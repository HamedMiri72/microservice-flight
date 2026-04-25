package com.hamedTech.controller;


import com.hamedTech.payload.request.LoginRequest;
import com.hamedTech.payload.request.SignupRequest;
import com.hamedTech.payload.response.AuthResponse;
import com.hamedTech.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {

        AuthResponse authResponse = authService.signup(signupRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

        AuthResponse authResponse = authService.login(loginRequest.getEmail(), loginRequest.getPassword());

        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }
}
