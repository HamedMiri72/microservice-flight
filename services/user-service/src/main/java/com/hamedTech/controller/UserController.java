package com.hamedTech.controller;


import com.hamedTech.model.User;
import com.hamedTech.payload.response.UserResponse;
import com.hamedTech.repository.UserRepository;
import com.hamedTech.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getUserProfile(
            @RequestHeader("X-User-Email") String email
    ){
        UserResponse userResponse = userService.getUserByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        UserResponse userResponse = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        List<UserResponse> userResponseList = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(userResponseList);
    }

}
