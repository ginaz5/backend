package com.example.backend.service;

import com.example.backend.controller.request.LoginRequest;
import com.example.backend.controller.response.UserResponse;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.AppUser;
import com.example.backend.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private AppUser testUser;
    private LoginRequest loginRequest;
    private UserResponse userResponse;

    @BeforeEach
    public void setUp() {
        testUser = new AppUser();
        testUser.setId(UUID.randomUUID());
        testUser.setUsername("testUser");
        testUser.setPassword("password123");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("password123");

        userResponse = new UserResponse();
        userResponse.setId(testUser.getId());
        userResponse.setUsername(testUser.getUsername());
    }

    @Test
    public void testLoginSuccess() {
        when(appUserRepository.findByUsername(any(String.class))).thenReturn(testUser);
        when(userMapper.toResponse(any(AppUser.class))).thenReturn(userResponse);

        Optional<UserResponse> result = userService.login(loginRequest);

        assertTrue(result.isPresent());
        assertEquals(testUser.getUsername(), result.get().getUsername());
        verify(appUserRepository, times(1)).findByUsername(loginRequest.getUsername());
        verify(userMapper, times(1)).toResponse(testUser);
    }

    @Test
    public void testLoginFailureWrongPassword() {
        when(appUserRepository.findByUsername(any(String.class))).thenReturn(testUser);

        loginRequest.setPassword("wrongPassword");

        Optional<UserResponse> result = userService.login(loginRequest);

        assertTrue(result.isEmpty());
        verify(appUserRepository, times(1)).findByUsername(loginRequest.getUsername());
        verify(userMapper, never()).toResponse(any(AppUser.class));
    }

    @Test
    public void testLoginFailureUserNotFound() {
        when(appUserRepository.findByUsername(any(String.class))).thenReturn(null);

        Optional<UserResponse> result = userService.login(loginRequest);

        assertTrue(result.isEmpty());
        verify(appUserRepository, times(1)).findByUsername(loginRequest.getUsername());
        verify(userMapper, never()).toResponse(any(AppUser.class));
    }

    @Test
    public void testGetUserByIdSuccess() {
        when(appUserRepository.findById(any(UUID.class))).thenReturn(Optional.of(testUser));
        when(userMapper.toResponse(any(AppUser.class))).thenReturn(userResponse);

        UserResponse result = userService.getUserById(testUser.getId());

        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
        verify(appUserRepository, times(1)).findById(testUser.getId());
        verify(userMapper, times(1)).toResponse(testUser);
    }

    @Test
    public void testGetUserByIdUserNotFound() {
        when(appUserRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(testUser.getId());
        });

        verify(appUserRepository, times(1)).findById(testUser.getId());
        verify(userMapper, never()).toResponse(any(AppUser.class));
    }
}