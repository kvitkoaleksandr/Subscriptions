package com.example.subscriptions.service;

import com.example.subscriptions.dto.*;

import java.util.List;

/**
 * Service interface for subscription-related operations.
 */
public interface SubscriptionService {

    /**
     * Adds a subscription for a user.
     *
     * @param userId the user ID
     * @param request the subscription creation request
     * @return created subscription DTO
     */
    SubscriptionDto addSubscription(Long userId, CreateSubscriptionRequest request);

    /**
     * Retrieves all subscriptions of a user.
     *
     * @param userId the user ID
     * @return list of subscription DTOs
     */
    List<SubscriptionDto> getUserSubscriptions(Long userId);

    /**
     * Deletes a subscription for a specific user.
     *
     * @param userId the user ID
     * @param subscriptionId the subscription ID
     */
    void deleteSubscription(Long userId, Long subscriptionId);

    /**
     * Returns top 3 most popular subscription services across all users.
     *
     * @return list of top subscription DTOs
     */
    List<SubscriptionDto> getTop3Subscriptions();
}