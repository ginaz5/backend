package com.example.backend.controller;

import com.example.backend.controller.request.LoginRequest;
import com.example.backend.controller.response.UserResponse;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UUID userId;
    private UserResponse userResponse;
    private LoginRequest loginRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();

        userResponse = new UserResponse();
        userResponse.setId(userId);
        userResponse.setUsername("testUser");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("password123");
    }

    @Nested
    class LoginTests {

        @Test
        public void testLoginSuccess() {
            when(userService.login(any(LoginRequest.class))).thenReturn(Optional.of(userResponse));

            ResponseEntity<?> response = userController.login(loginRequest);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(userResponse, response.getBody());
            verify(userService, times(1)).login(loginRequest);
        }

        @Test
        public void testLoginUserNotFound() {
            when(userService.login(any(LoginRequest.class))).thenReturn(Optional.empty());

            UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
                userController.login(loginRequest);
            });

            assertEquals("User not found", exception.getMessage());
            verify(userService, times(1)).login(loginRequest);
        }
    }

    @Nested
    class GetUserByIdTests {

        @Test
        public void testGetUserByIdSuccess() {
            when(userService.getUserById(any(UUID.class))).thenReturn(userResponse);

            ResponseEntity<UserResponse> response = userController.getUserById(userId);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(userResponse, response.getBody());
            verify(userService, times(1)).getUserById(userId);
        }

        @Test
        public void testGetUserByIdNotFound() {
            when(userService.getUserById(any(UUID.class))).thenThrow(new UserNotFoundException("User not found"));

            UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
                userController.getUserById(userId);
            });

            assertEquals("User not found", exception.getMessage());
            verify(userService, times(1)).getUserById(userId);
        }
    }
}