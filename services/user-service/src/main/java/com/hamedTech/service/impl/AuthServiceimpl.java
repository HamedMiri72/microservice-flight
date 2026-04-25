package com.hamedTech.service.impl;

import com.hamedTech.enums.Role;
import com.hamedTech.model.User;
import com.hamedTech.payload.request.SignupRequest;
import com.hamedTech.payload.response.AuthResponse;
import com.hamedTech.repository.UserRepository;
import com.hamedTech.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceimpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    @Override
    public AuthResponse login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email"));


        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        String jwt = jwtService.generateToken(Map.of("role", user.getRole()), user);

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return AuthResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .token(jwt)
                .message("you have been logged in")
                .lastLogin(user.getLastLogin())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    public AuthResponse signup(SignupRequest request) {

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists");
        }

        if(request.getRole() == Role.ROLE_SYSTEM_ADMIN) {
            throw new RuntimeException("you can't sign up with system admin");
        }

        User newUser = User.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(request.getPassword())
                .role(request.getRole())
                .lastLogin(LocalDateTime.now())
                .build();

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        newUser.setPassword(encodedPassword);

        User savedUser = userRepository.save(newUser);

        String jwt = jwtService.generateToken(Map.of("role", request.getRole()), newUser);

        return AuthResponse.builder()
                .id(savedUser.getId())
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .token(jwt)
                .message("Your account has been signed in")
                .createdAt(savedUser.getCreatedAt())
                .lastLogin(LocalDateTime.now())
                .build();
    }
}
