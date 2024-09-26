package com.example.backend.controller;

import com.example.backend.controller.request.LoginRequest;
import com.example.backend.controller.response.UserResponse;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Call the login service to authenticate and get the user response
        Optional<UserResponse> userResponseOptional = userService.login(loginRequest);

        if (userResponseOptional.isPresent()) {
            // Return the UserResponse if authentication was successful
            return ResponseEntity.ok(userResponseOptional.get());
        } else {
            // Return 401 if authentication failed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}

