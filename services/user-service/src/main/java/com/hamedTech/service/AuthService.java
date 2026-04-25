package com.hamedTech.service;


import com.hamedTech.payload.request.SignupRequest;
import com.hamedTech.payload.response.AuthResponse;

public interface AuthService {

    AuthResponse login(String email, String password);

    AuthResponse signup(SignupRequest request);
}
