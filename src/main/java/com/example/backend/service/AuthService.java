package com.example.backend.service;

import com.example.backend.controller.response.BookResponse;
import com.example.backend.controller.response.InventoryBookResponse;
import com.example.backend.controller.response.UserResponse;
import com.example.backend.model.AppUser;
import com.example.backend.repository.InventoryRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;  // Assuming you need it for user inventories
    private final JwtService jwtService;  // Service to generate JWT tokens
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, InventoryRepository inventoryRepository,
                       JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.inventoryRepository = inventoryRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse authenticate(String username, String password) {
        Optional<AppUser> userOpt = Optional.ofNullable(userRepository.findByUsername(username));

        if (userOpt.isPresent()) {
            AppUser user = userOpt.get();

            // Check if the password matches (hashed in DB)
            if (passwordEncoder.matches(password, user.getPassword())) {

                // Generate the JWT token for this user
                String token = jwtService.generateToken(user);

                // Populate the inventories (this could be empty if not needed for login)
                List<InventoryBookResponse> inventories = inventoryRepository.findByUserId(user.getId())
                        .stream()
                        .map(inventory -> new InventoryBookResponse(
                                inventory.getId(),
                                inventory.getLoanDate(),
                                (BookResponse.builder()
                                        .id(inventory.getBook().getId())
                                        .title(inventory.getBook().getTitle())
                                        .author(inventory.getBook().getAuthor())
                                        .image(inventory.getBook().getImage())
                                        .build()
                                )
                        ))
                        .collect(Collectors.toList());

                // Return the UserResponse with token and other user details
                return new UserResponse(user.getId(), user.getUsername(), user.getRole(), inventories, token);
            } else {
                throw new RuntimeException("Invalid username or password.");
            }
        } else {
            throw new RuntimeException("Invalid username or password.");
        }
    }
}