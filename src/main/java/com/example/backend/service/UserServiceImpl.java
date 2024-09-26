package com.example.backend.service;

import com.example.backend.controller.request.LoginRequest;
import com.example.backend.controller.response.UserResponse;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.AppUser;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse login(LoginRequest loginRequest) {
        // Assuming a simple username and password check for demonstration purposes
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // Replace with actual authentication logic
        if ("admin".equals(username) && "password".equals(password)) {
            // Fetch user from database
            AppUser user = userRepository.findByUsername(username);
            if (user != null) {
                // Map user to response and return
                return userMapper.toResponse(user);
            } else {
                throw new RuntimeException("User not found");
            }
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    @Override
    public UserResponse getUserById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}