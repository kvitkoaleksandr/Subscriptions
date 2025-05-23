package com.example.subscriptions.repository;

import com.example.subscriptions.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for {@link User} entity.
 * Provides basic CRUD operations using Spring Data JPA.
 */
public interface UserRepository extends JpaRepository<User, Long> {
}