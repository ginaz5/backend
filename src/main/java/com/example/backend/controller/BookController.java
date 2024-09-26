package com.example.backend.controller;

import com.example.backend.controller.request.BookActionRequest;

import com.example.backend.controller.response.BookResponse;
import com.example.backend.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PutMapping("/return")
    public ResponseEntity<Void> returnBook(@RequestBody BookActionRequest request) {
        bookService.returnBook(request);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/borrow")
    public ResponseEntity<Void> borrowBook(@RequestBody BookActionRequest request) {
        bookService.borrowBook(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable UUID id) {
        BookResponse book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getBooks(@RequestParam(defaultValue = "0") int offset,
                                                       @RequestParam(defaultValue = "10") int limit) {
        List<BookResponse> books = bookService.getBooks(offset, limit);
        return ResponseEntity.ok(books);
    }
}