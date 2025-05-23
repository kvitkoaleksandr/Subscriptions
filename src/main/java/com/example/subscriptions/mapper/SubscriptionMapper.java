package com.example.subscriptions.mapper;

import com.example.subscriptions.dto.*;
import com.example.subscriptions.entity.*;

/**
 * Utility class for mapping between {@link Subscription} entities and {@link SubscriptionDto} objects.
 */
public class SubscriptionMapper {

    /**
     * Converts a {@link Subscription} entity to a {@link SubscriptionDto}.
     *
     * @param sub the subscription entity
     * @return the subscription DTO
     */
    public static SubscriptionDto toDto(Subscription sub) {
        if (sub == null) return null;

        return SubscriptionDto.builder()
                .id(sub.getId())
                .serviceName(sub.getServiceName())
                .build();
    }

    /**
     * Converts a {@link CreateSubscriptionRequest} to a {@link Subscription} entity.
     *
     * @param request the subscription creation request
     * @param user the user entity
     * @return the subscription entity
     */
    public static Subscription toEntity(CreateSubscriptionRequest request, User user) {
        return Subscription.builder()
                .serviceName(request.getServiceName())
                .user(user)
                .build();
    }
}