package com.example.backend.repository;

import com.example.backend.model.AppUser;
import com.example.backend.model.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository appUserRepository;

    private AppUser user;

    @BeforeEach
    public void setUp() {
        user = new AppUser();
        user.setUsername("testUser");
        user.setPassword("password123");
        user.setRole("USER");

        appUserRepository.save(user);
    }

    @Nested
    class FindByUsernameTests {

        @Test
        public void testFindByUsernameSuccess() {
            AppUser foundUser = appUserRepository.findByUsername("testUser");

            assertNotNull(foundUser);
            assertEquals("testUser", foundUser.getUsername());
            assertEquals("password123", foundUser.getPassword());
        }

        @Test
        public void testFindByUsernameNotFound() {
            AppUser foundUser = appUserRepository.findByUsername("nonExistentUser");
            assertNull(foundUser);
        }
    }
}