package com.example.backend.service;


import com.example.backend.controller.request.BookActionRequest;
import com.example.backend.controller.response.BookResponse;
import com.example.backend.mapper.BookMapper;
import com.example.backend.model.Book;
import com.example.backend.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public void returnBook(BookActionRequest request) {
        // Logic for returning the book
        // need to check the user,
        // if the logged-in user matches the borrowed user_id in the database clean the timestamp and return 202
        // else return 409 with error message "Book cannot be returned by another user".
        // error handing if there is no book to return, then return 404 book not found

    }

    @Override
    public void borrowBook(BookActionRequest request) {
        // Logic for borrowing the book
        // if the book is available in inventory then update the user_id and timestamp and success return 202 means user successfully borrowed the book
        // else return 409 with error message "Book is already borrowed".
        // error handling if there is no book to borrow, then return 404 book not found
    }

    @Override
    public BookResponse getBookById(UUID id) {
        return bookRepository.findById(id)
                .map(bookMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    @Override
    public List<BookResponse> getBooks(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Book> booksPage = bookRepository.findAll(pageable);
        return booksPage.stream()
                .map(bookMapper::toResponse)
                .collect(Collectors.toList());
    }
}