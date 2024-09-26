package com.example.backend.controller;

import com.example.backend.controller.request.BookActionRequest;

import com.example.backend.controller.response.BookResponse;
import com.example.backend.exception.BookAlreadyBorrowedException;
import com.example.backend.exception.BookNotFoundException;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PutMapping("/return")
    public ResponseEntity<String> returnBook(@RequestBody BookActionRequest request) {
        try {
            bookService.returnBook(request);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build(); // 202 Accepted
        } catch (BookNotFoundException e) {
            logger.error("Book not found: {}", e.getMessage()); // Log error to the console
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404 Not Found with message
        } catch (RuntimeException e) { // Generic catch for other potential runtime issues
            logger.error("Conflict error: {}", e.getMessage()); // Log error to the console
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // 409 Conflict with message
        }
    }

    @PutMapping("/borrow")
    public ResponseEntity<String> borrowBook(@RequestBody BookActionRequest request) {
        try {
            bookService.borrowBook(request);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build(); // 202 Accepted
        } catch (BookNotFoundException e) {
            logger.error("Book not found: {}", e.getMessage()); // Log error to the console
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404 Not Found with message
        } catch (BookAlreadyBorrowedException e) {
            logger.error("Book is already borrowed: {}", e.getMessage()); // Log error to the console
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // 409 Conflict with message
        } catch (UserNotFoundException e) {
            logger.error("User not found: {}", e.getMessage()); // Log error to the console
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404 Not Found for user
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getBookById(@PathVariable UUID id) {
        try {
            BookResponse book = bookService.getBookById(id);
            return ResponseEntity.ok(book); // 200 OK
        } catch (BookNotFoundException e) {
            logger.error("Book not found: {}", e.getMessage()); // Log error to the console
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404 Not Found with message
        }
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getBooks(@RequestParam(defaultValue = "0") int offset,
                                                       @RequestParam(defaultValue = "10") int limit) {
        List<BookResponse> books = bookService.getBooks(offset, limit);
        return ResponseEntity.ok(books);
    }
}