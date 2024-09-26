package com.example.backend.controller;

import com.example.backend.controller.request.LoginRequest;
import com.example.backend.controller.response.UserResponse;
import com.example.backend.model.AppUser;
import com.example.backend.service.AuthService;
import com.example.backend.service.UserService;
import org.apache.catalina.Authenticator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate user using the AuthService
            UserResponse userResponse = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

            // Return user details (and token if applicable)
            return ResponseEntity.ok(userResponse);
        } catch (Exception ex) {
            // Return 401 if authentication fails
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}