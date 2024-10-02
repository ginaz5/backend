package com.example.backend.repository;

import com.example.backend.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    public void setUp() {
        // Initialize and save multiple Book entities for paging tests
        bookRepository.saveAll(List.of(
                createTestBook("Test Book 1", "Author 1"),
                createTestBook("Test Book 2", "Author 2"),
                createTestBook("Test Book 3", "Author 3"),
                createTestBook("Test Book 4", "Author 4"),
                createTestBook("Test Book 5", "Author 5")
        ));
    }

    private Book createTestBook(String title, String author) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        return book;
    }

    @Test
    public void testFindAllWithPagination() {
        Pageable pageable = PageRequest.of(0, 3);

        Page<Book> bookPage = bookRepository.findAll(pageable);

        assertNotNull(bookPage);
        assertEquals(3, bookPage.getContent().size());  // There should be 3 books in the first page
        assertEquals(2, bookPage.getTotalPages());      // Total of 5 books, so 2 pages expected
        assertEquals(5, bookPage.getTotalElements());   // Total number of books should be 5
    }

    @Test
    public void testFindSecondPage() {
        Pageable pageable = PageRequest.of(1, 3);

        Page<Book> bookPage = bookRepository.findAll(pageable);

        assertNotNull(bookPage);
        assertEquals(2, bookPage.getContent().size());  // There should be 2 books in the second page (5 books total)
        assertEquals(2, bookPage.getTotalPages());      // Total of 2 pages expected
        assertEquals(5, bookPage.getTotalElements());   // Total number of books should be 5
    }
}