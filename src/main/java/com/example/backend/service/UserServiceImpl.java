package com.example.backend.service;

import com.example.backend.controller.request.LoginRequest;
import com.example.backend.controller.response.UserResponse;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.AppUser;
import com.example.backend.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AppUserRepository appUserRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<UserResponse> login(LoginRequest loginRequest) {
        return authenticate(loginRequest)
                .map(userMapper::toResponse);
    }

    private Optional<AppUser> authenticate(LoginRequest loginRequest) {
        return Optional.ofNullable(appUserRepository.findByUsername(loginRequest.getUsername()))
                .filter(user -> user.getPassword().equals(loginRequest.getPassword()));
    }

    @Override
    public UserResponse getUserById(UUID id) {
        return appUserRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));
    }
}