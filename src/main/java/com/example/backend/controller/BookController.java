package com.example.backend.controller;

import com.example.backend.controller.request.BookActionRequest;

import com.example.backend.controller.response.BookResponse;
import com.example.backend.exception.BookAlreadyBorrowedException;
import com.example.backend.exception.BookNotFoundException;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Return a book", description = "Return a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Accepted"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Value must be a Guid"),
            @ApiResponse(responseCode = "404", description = "Not found - The book is not found"),
            @ApiResponse(responseCode = "409", description = "Conflict - The book can't be returned by another user")
    })
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
    @Operation(summary = "Borrow a book", description = "Borrow a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Accepted"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Value must be a Guid"),
            @ApiResponse(responseCode = "404", description = "Not found - The book is not found"),
            @ApiResponse(responseCode = "409", description = "Conflict - The book is already borrowed")
    })
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
    @Operation(summary = "Get a book by Id", description = "Returns a book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Value must be a Guid"),
            @ApiResponse(responseCode = "404", description = "Not found - The book is not found")
    })
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
    @Operation(summary = "Get all books", description = "Returns books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public ResponseEntity<List<BookResponse>> getBooks(@RequestParam(defaultValue = "0") int offset,
                                                       @RequestParam(defaultValue = "10") int limit) {
        List<BookResponse> books = bookService.getBooks(offset, limit);
        return ResponseEntity.ok(books);
    }
}