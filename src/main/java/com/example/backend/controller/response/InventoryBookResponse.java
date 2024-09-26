package com.example.backend.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryBookResponse {
    private UUID id;
    private LocalDateTime loanDate;
    private BookResponse book;
}