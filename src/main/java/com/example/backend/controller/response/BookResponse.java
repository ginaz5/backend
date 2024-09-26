package com.example.backend.controller.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class BookResponse {
    private UUID id;
    private String title;
    private String author;
    private String image;
    private List<InventoryResponse> inventories;
}