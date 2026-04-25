package com.hamedTech.service;

import com.hamedTech.model.User;
import com.hamedTech.payload.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse getUserByEmail(String email);
    UserResponse getUserById(Long id);
    List<UserResponse> getAllUsers();

}
