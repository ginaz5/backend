package com.example.backend.repository;

import com.example.backend.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    AppUser findByUsername(String username);

    @Query("SELECT u FROM AppUser u LEFT JOIN FETCH u.inventories WHERE u.id = :userId")
    Optional<AppUser> findByIdWithInventories(@Param("userId") UUID userId);
}