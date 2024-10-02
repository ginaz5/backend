package com.example.backend.controller;

import com.example.backend.controller.request.BookActionRequest;
import com.example.backend.controller.response.BookResponse;
import com.example.backend.exception.BookAlreadyBorrowedException;
import com.example.backend.exception.BookNotFoundException;
import com.example.backend.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private BookActionRequest bookActionRequest;
    private UUID bookId;
    private BookResponse bookResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bookActionRequest = new BookActionRequest();
        bookId = UUID.randomUUID();

        bookResponse = new BookResponse();
        bookResponse.setId(bookId);
        bookResponse.setTitle("Test Book");
    }

    @Nested
    class ReturnBookTests {

        @Test
        public void testReturnBookSuccess() {
            doNothing().when(bookService).returnBook(any(BookActionRequest.class));

            ResponseEntity<String> response = bookController.returnBook(bookActionRequest);

            assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
            verify(bookService, times(1)).returnBook(bookActionRequest);
        }

        @Test
        public void testReturnBookNotFound() {
            doThrow(new BookNotFoundException("Book not found")).when(bookService).returnBook(any(BookActionRequest.class));

            ResponseEntity<String> response = bookController.returnBook(bookActionRequest);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals("Book not found", response.getBody());
            verify(bookService, times(1)).returnBook(bookActionRequest);
        }

        @Test
        public void testReturnBookConflict() {
            doThrow(new BookAlreadyBorrowedException("Book is already borrowed")).when(bookService).returnBook(any(BookActionRequest.class));

            ResponseEntity<String> response = bookController.returnBook(bookActionRequest);

            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertEquals("Book is already borrowed", response.getBody());
            verify(bookService, times(1)).returnBook(bookActionRequest);
        }
    }

    @Nested
    class BorrowBookTests {

        @Test
        public void testBorrowBookSuccess() {
            doNothing().when(bookService).borrowBook(any(BookActionRequest.class));

            ResponseEntity<String> response = bookController.borrowBook(bookActionRequest);

            assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
            verify(bookService, times(1)).borrowBook(bookActionRequest);
        }

        @Test
        public void testBorrowBookNotFound() {
            doThrow(new BookNotFoundException("Book not found")).when(bookService).borrowBook(any(BookActionRequest.class));

            ResponseEntity<String> response = bookController.borrowBook(bookActionRequest);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals("Book not found", response.getBody());
            verify(bookService, times(1)).borrowBook(bookActionRequest);
        }

        @Test
        public void testBorrowBookConflict() {
            doThrow(new BookAlreadyBorrowedException("Book is already borrowed")).when(bookService).borrowBook(any(BookActionRequest.class));

            ResponseEntity<String> response = bookController.borrowBook(bookActionRequest);

            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertEquals("Book is already borrowed", response.getBody());
            verify(bookService, times(1)).borrowBook(bookActionRequest);
        }
    }

    @Nested
    class GetBookByIdTests {

        @Test
        public void testGetBookByIdSuccess() {
            when(bookService.getBookById(bookId)).thenReturn(bookResponse);

            ResponseEntity<Object> response = bookController.getBookById(bookId);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(bookResponse, response.getBody());
            verify(bookService, times(1)).getBookById(bookId);
        }

        @Test
        public void testGetBookByIdNotFound() {
            when(bookService.getBookById(bookId)).thenThrow(new BookNotFoundException("Book not found"));

            ResponseEntity<Object> response = bookController.getBookById(bookId);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals("Book not found", response.getBody());
            verify(bookService, times(1)).getBookById(bookId);
        }
    }

    @Nested
    class GetBooksTests {

        @Test
        public void testGetBooksSuccess() {
            List<BookResponse> bookList = List.of(bookResponse, new BookResponse());
            when(bookService.getBooks(0, 10)).thenReturn(bookList);

            ResponseEntity<List<BookResponse>> response = bookController.getBooks(0, 10);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(bookList, response.getBody());
            verify(bookService, times(1)).getBooks(0, 10);
        }
    }
}