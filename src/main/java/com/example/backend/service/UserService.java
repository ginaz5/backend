package com.example.backend.service;

import com.example.backend.controller.request.LoginRequest;
import com.example.backend.controller.response.UserResponse;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<UserResponse> login(LoginRequest loginRequest);
    UserResponse getUserById(UUID id);
}