package com.example.subscriptions.service;

import com.example.subscriptions.dto.*;

import java.util.List;

public interface SubscriptionService {

    SubscriptionDto addSubscription(Long userId, CreateSubscriptionRequest request);

    List<SubscriptionDto> getUserSubscriptions(Long userId);

    void deleteSubscription(Long userId, Long subscriptionId);

    List<SubscriptionDto> getTop3Subscriptions();
}