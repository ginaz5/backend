package com.example.backend.controller.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class InventoryResponse {
    private UUID id;
    private LocalDateTime loanDate;
    private UUID bookId;
    private UUID userId;
}