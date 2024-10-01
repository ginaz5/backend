package com.example.backend.service;


import com.example.backend.controller.BookController;
import com.example.backend.controller.request.BookActionRequest;
import com.example.backend.controller.response.BookResponse;
import com.example.backend.exception.BookAlreadyBorrowedException;
import com.example.backend.exception.BookNotFoundException;
import com.example.backend.exception.BookReturnConflictException;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.mapper.BookMapper;
import com.example.backend.model.AppUser;
import com.example.backend.model.Book;
import com.example.backend.model.Inventory;
import com.example.backend.repository.AppUserRepository;
import com.example.backend.repository.BookRepository;
import com.example.backend.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final InventoryRepository inventoryRepository;
    private final AppUserRepository appUserRepository;
    private final BookMapper bookMapper;

    @Override
    public void returnBook(BookActionRequest request) {
        // Find the inventory entry by Id
        Inventory inventory = inventoryRepository.findById(request.getInventoryId())
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        if (inventory.getUser() == null || !request.getUserId().equals(inventory.getUser().getId())) {
            throw new BookReturnConflictException("Book cannot be returned by another user");
        }

        inventory.setUser(null);
        inventory.setLoanDate(null);

        inventoryRepository.save(inventory);
    }

    @Override
    public void borrowBook(BookActionRequest request) {
        Inventory inventory = inventoryRepository.findById(request.getInventoryId())
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        if (inventory.getUser() != null) {
            throw new BookAlreadyBorrowedException("Book is already borrowed");
        }

        AppUser user = appUserRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User with ID " + request.getUserId() + " not found"));

        inventory.setUser(user);
        inventory.setLoanDate(LocalDateTime.now());

        inventoryRepository.save(inventory);
    }


    @Override
    public BookResponse getBookById(UUID id) {
        return bookRepository.findById(id)
                .map(bookMapper::toResponse)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
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