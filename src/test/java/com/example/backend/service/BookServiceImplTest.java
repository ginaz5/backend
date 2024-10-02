package com.example.backend.service;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private BookActionRequest bookActionRequest;
    private Inventory inventory;
    private AppUser user;
    private Book book;
    private BookResponse bookResponse;

    @BeforeEach
    public void setUp() {
        bookActionRequest = new BookActionRequest();
        bookActionRequest.setInventoryId(UUID.randomUUID());
        bookActionRequest.setUserId(UUID.randomUUID());

        inventory = new Inventory();
        inventory.setId(bookActionRequest.getInventoryId());

        user = new AppUser();
        user.setId(bookActionRequest.getUserId());

        book = new Book();
        book.setId(UUID.randomUUID());
        book.setTitle("Test Book");

        bookResponse = new BookResponse();
        bookResponse.setId(book.getId());
        bookResponse.setTitle(book.getTitle());
    }

    @Nested
    class BorrowBookTests {
        @Test
        public void testBorrowBookSuccess() {
            when(inventoryRepository.findById(bookActionRequest.getInventoryId())).thenReturn(Optional.of(inventory));
            when(appUserRepository.findById(bookActionRequest.getUserId())).thenReturn(Optional.of(user));

            bookService.borrowBook(bookActionRequest);

            assertEquals(user, inventory.getUser());
            assertNotNull(inventory.getLoanDate());
            verify(inventoryRepository, times(1)).save(inventory);
        }

        @Test
        public void testBorrowBookAlreadyBorrowed() {
            inventory.setUser(user);
            when(inventoryRepository.findById(bookActionRequest.getInventoryId())).thenReturn(Optional.of(inventory));

            assertThrows(BookAlreadyBorrowedException.class, () -> bookService.borrowBook(bookActionRequest));
            verify(inventoryRepository, never()).save(inventory);
        }

        @Test
        public void testBorrowBookUserNotFound() {
            when(inventoryRepository.findById(bookActionRequest.getInventoryId())).thenReturn(Optional.of(inventory));
            when(appUserRepository.findById(bookActionRequest.getUserId())).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> bookService.borrowBook(bookActionRequest));
            verify(inventoryRepository, never()).save(inventory);
        }
    }

    @Nested
    class ReturnBookTests {
        @Test
        public void testReturnBookSuccess() {
            inventory.setUser(user);
            when(inventoryRepository.findById(bookActionRequest.getInventoryId())).thenReturn(Optional.of(inventory));

            bookService.returnBook(bookActionRequest);

            assertNull(inventory.getUser());
            assertNull(inventory.getLoanDate());
            verify(inventoryRepository, times(1)).save(inventory);
        }

        @Test
        public void testReturnBookConflict() {
            AppUser anotherUser = new AppUser();
            anotherUser.setId(UUID.randomUUID());
            inventory.setUser(anotherUser);
            when(inventoryRepository.findById(bookActionRequest.getInventoryId())).thenReturn(Optional.of(inventory));

            assertThrows(BookReturnConflictException.class, () -> bookService.returnBook(bookActionRequest));
            verify(inventoryRepository, never()).save(inventory);
        }
    }

    @Nested
    class GetBookByIdTests {
        @Test
        public void testGetBookByIdSuccess() {
            when(bookRepository.findById(any(UUID.class))).thenReturn(Optional.of(book));
            when(bookMapper.toResponse(any(Book.class))).thenReturn(bookResponse);

            BookResponse result = bookService.getBookById(book.getId());

            assertNotNull(result);
            assertEquals(book.getId(), result.getId());
            verify(bookRepository, times(1)).findById(book.getId());
            verify(bookMapper, times(1)).toResponse(book);
        }

        @Test
        public void testGetBookByIdNotFound() {
            when(bookRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

            assertThrows(BookNotFoundException.class, () -> bookService.getBookById(UUID.randomUUID()));
            verify(bookRepository, times(1)).findById(any(UUID.class));
            verify(bookMapper, never()).toResponse(any(Book.class));
        }
    }

    @Nested
    class GetBooksTests {
        @Test
        public void testGetBooksSuccess() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Book> bookPage = new PageImpl<>(Arrays.asList(book));
            when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);
            when(bookMapper.toResponse(any(Book.class))).thenReturn(bookResponse);

            List<BookResponse> books = bookService.getBooks(0, 10);

            assertNotNull(books);
            assertEquals(1, books.size());
            assertEquals(book.getId(), books.get(0).getId());
            verify(bookRepository, times(1)).findAll(pageable);
            verify(bookMapper, times(1)).toResponse(book);
        }
    }
}