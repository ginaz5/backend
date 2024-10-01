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

    public Optional<UserResponse> login(LoginRequest loginRequest) {
        // Authenticate the user
        Optional<AppUser> appUserOptional = authenticate(loginRequest);

        if (appUserOptional.isPresent()) {
            // Map AppUser to UserResponse and return
            return Optional.of(userMapper.toResponse(appUserOptional.get()));
        }

        return Optional.empty(); // Return empty if authentication failed
    }

    private Optional<AppUser> authenticate(LoginRequest loginRequest) {
        // Find the user by username
        Optional<AppUser> userOptional = Optional.ofNullable(appUserRepository.findByUsername(loginRequest.getUsername()));

        if (userOptional.isPresent()) {
            AppUser user = userOptional.get();
            // Compare the plain-text password
            if (user.getPassword().equals(loginRequest.getPassword())) {
                return Optional.of(user); // Return the user if password matches
            }
        }

        return Optional.empty(); // Username not found or password didn't match
    }


    @Override
    public UserResponse getUserById(UUID id) {
        return appUserRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
    }
}