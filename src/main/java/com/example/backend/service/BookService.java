package com.example.backend.service;

import com.example.backend.controller.request.BookActionRequest;
import com.example.backend.controller.response.BookResponse;

import java.util.List;
import java.util.UUID;

public interface BookService {
    void returnBook(BookActionRequest returnRequest);
    void borrowBook(BookActionRequest borrowRequest);
    BookResponse getBookById(UUID id);
    List<BookResponse> getBooks(int offset, int limit);
}