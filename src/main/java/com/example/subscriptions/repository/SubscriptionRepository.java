package com.example.subscriptions.repository;

import com.example.subscriptions.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for {@link Subscription} entity.
 * Provides basic CRUD operations and custom queries.
 */
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    /**
     * Retrieves all subscriptions belonging to a specific user.
     *
     * @param userId the user ID
     * @return list of subscriptions
     */
    List<Subscription> findByUserId(Long userId);
}