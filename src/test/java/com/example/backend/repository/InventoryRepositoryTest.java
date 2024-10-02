package com.example.backend.repository;

import com.example.backend.model.AppUser;
import com.example.backend.model.Book;
import com.example.backend.model.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class InventoryRepositoryTest {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    private Book book;
    private AppUser user;
    private Inventory inventory;

    @BeforeEach
    public void setUp() {
        // Initialize Book and User for each test case
        book = createTestBook();
        user = createTestUser();

        // Set up default inventory object for reuse in tests
        inventory = createTestInventory(book, user);
    }

    private Book createTestBook() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        return bookRepository.save(book);
    }

    private AppUser createTestUser() {
        AppUser user = new AppUser();
        user.setUsername("testUser");
        user.setPassword("password123");
        user.setRole("USER");
        return appUserRepository.save(user);
    }

    private Inventory createTestInventory(Book book, AppUser user) {
        Inventory inventory = new Inventory();
        inventory.setBook(book);
        inventory.setUser(user);
        inventory.setLoanDate(LocalDateTime.now());
        return inventoryRepository.save(inventory);
    }

    @Nested
    class InventoryCRUDTests {

        @Test
        public void testSaveInventory() {
            Inventory savedInventory = inventoryRepository.save(inventory);

            assertNotNull(savedInventory.getId());
            assertEquals(book.getId(), savedInventory.getBook().getId());
            assertEquals(user.getId(), savedInventory.getUser().getId());
            assertNotNull(savedInventory.getLoanDate());
        }

        @Test
        public void testFindById() {
            Optional<Inventory> foundInventory = inventoryRepository.findById(inventory.getId());

            assertTrue(foundInventory.isPresent());
            Inventory retrievedInventory = foundInventory.get();
            assertEquals(inventory.getId(), retrievedInventory.getId());
            assertEquals(book.getId(), retrievedInventory.getBook().getId());
            assertEquals(user.getId(), retrievedInventory.getUser().getId());
            assertEquals(inventory.getLoanDate(), retrievedInventory.getLoanDate());
        }

        @Test
        public void testDeleteInventory() {
            Optional<Inventory> foundInventory = inventoryRepository.findById(inventory.getId());
            assertTrue(foundInventory.isPresent());

            inventoryRepository.delete(inventory);

            Optional<Inventory> deletedInventory = inventoryRepository.findById(inventory.getId());
            assertFalse(deletedInventory.isPresent());
        }

        @Test
        public void testUpdateInventory() {
            Optional<Inventory> savedInventory = inventoryRepository.findById(inventory.getId());
            assertTrue(savedInventory.isPresent());

            Inventory inventoryToUpdate = savedInventory.get();
            // ignore sub-millisecond differences
            LocalDateTime updatedLoanDate = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.MILLIS);
            inventoryToUpdate.setLoanDate(updatedLoanDate);

            inventoryRepository.save(inventoryToUpdate);

            Optional<Inventory> updatedInventory = inventoryRepository.findById(inventory.getId());
            assertTrue(updatedInventory.isPresent());
            Inventory retrievedInventory = updatedInventory.get();

            assertEquals(inventory.getId(), retrievedInventory.getId());
            assertEquals(book.getId(), retrievedInventory.getBook().getId());
            assertEquals(user.getId(), retrievedInventory.getUser().getId());

            assertEquals(updatedLoanDate, retrievedInventory.getLoanDate());
        }
    }
}