package com.example.backend.controller;

import com.example.backend.controller.request.LoginRequest;
import com.example.backend.controller.response.UserResponse;
import com.example.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "User Login", description = "User Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully login"),
            @ApiResponse(responseCode = "404", description = "Not found - The user is not found")
    })
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Call the login service to authenticate and get the user response
        Optional<UserResponse> userResponseOptional = userService.login(loginRequest);

        if (userResponseOptional.isPresent()) {
            // Return the UserResponse if authentication was successful
            return ResponseEntity.ok(userResponseOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found - The user is not found");
        }
    }
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The user is not found")
    })
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}

