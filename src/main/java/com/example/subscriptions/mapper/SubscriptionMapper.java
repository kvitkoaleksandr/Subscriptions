package com.example.subscriptions.mapper;

import com.example.subscriptions.dto.*;
import com.example.subscriptions.entity.*;

public class SubscriptionMapper {

    public static SubscriptionDto toDto(Subscription sub) {
        if (sub == null) return null;

        return SubscriptionDto.builder()
                .id(sub.getId())
                .serviceName(sub.getServiceName())
                .build();
    }

    public static Subscription toEntity(CreateSubscriptionRequest request, User user) {
        return Subscription.builder()
                .serviceName(request.getServiceName())
                .user(user)
                .build();
    }
}