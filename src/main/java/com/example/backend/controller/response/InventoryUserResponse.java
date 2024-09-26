package com.example.backend.controller.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryUserResponse {
    private UUID id;
    private LocalDateTime loanDate;
    private UserResponse user;
}