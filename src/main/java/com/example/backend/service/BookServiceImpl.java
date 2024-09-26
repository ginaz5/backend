package com.example.backend.service;


import com.example.backend.controller.BookController;
import com.example.backend.controller.request.BookActionRequest;
import com.example.backend.controller.response.BookResponse;
import com.example.backend.exception.BookAlreadyBorrowedException;
import com.example.backend.exception.BookNotFoundException;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.mapper.BookMapper;
import com.example.backend.model.AppUser;
import com.example.backend.model.Book;
import com.example.backend.model.Inventory;
import com.example.backend.repository.AppUserRepository;
import com.example.backend.repository.BookRepository;
import com.example.backend.repository.InventoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final InventoryRepository inventoryRepository;
    private final AppUserRepository appUserRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, InventoryRepository inventoryRepository,
                           AppUserRepository appUserRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.inventoryRepository = inventoryRepository;
        this.appUserRepository = appUserRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public void returnBook(BookActionRequest request) {
        // Find the inventory entry by bookId
        Inventory inventory = inventoryRepository.findById(request.getInventoryId())
                .orElseThrow(() -> new BookNotFoundException("Book not found in inventory"));

        // Check if the logged-in user matches the borrowed user
        if (inventory.getUser() == null || !request.getUserId().equals(inventory.getUser().getId())) {
            throw new RuntimeException("Book cannot be returned by another user");
        }

        // Clear the userId and loanDate to mark the book as returned
        inventory.setUser(null);
        inventory.setLoanDate(null);

        // Save the updated inventory
        inventoryRepository.save(inventory);
    }

    @Override
    public void borrowBook(BookActionRequest request) {
        // Find the inventory entry by bookId
        Inventory inventory = inventoryRepository.findById(request.getInventoryId())
                .orElseThrow(() -> new BookNotFoundException("Book not found in inventory"));

        // Check if the book is already borrowed
        if (inventory.getUser() != null) {
            throw new BookAlreadyBorrowedException("Book is already borrowed");
        }

        // Retrieve the AppUser from the database using the userId
        AppUser user = appUserRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Set the user and loanDate to mark the book as borrowed
        inventory.setUser(user);
        inventory.setLoanDate(LocalDateTime.now());

        // Save the updated inventory
        inventoryRepository.save(inventory);
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