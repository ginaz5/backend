package com.example.backend.service;

import com.example.backend.controller.request.LoginRequest;
import com.example.backend.controller.response.UserResponse;

import java.util.UUID;

public interface UserService {
    UserResponse login(LoginRequest loginRequest);
    UserResponse getUserById(UUID id);
}