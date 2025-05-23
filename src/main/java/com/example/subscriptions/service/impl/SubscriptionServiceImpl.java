package com.example.subscriptions.service.impl;

import com.example.subscriptions.dto.*;
import com.example.subscriptions.entity.*;
import com.example.subscriptions.exception.ResourceNotFoundException;
import com.example.subscriptions.mapper.SubscriptionMapper;
import com.example.subscriptions.repository.SubscriptionRepository;
import com.example.subscriptions.repository.UserRepository;
import com.example.subscriptions.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link SubscriptionService}.
 */
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Override
    public SubscriptionDto addSubscription(Long userId, CreateSubscriptionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Subscription subscription = SubscriptionMapper.toEntity(request, user);
        return SubscriptionMapper.toDto(subscriptionRepository.save(subscription));
    }

    @Override
    public List<SubscriptionDto> getUserSubscriptions(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        return subscriptionRepository.findByUserId(userId).stream()
                .map(SubscriptionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSubscription(Long userId, Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

        if (!Objects.equals(subscription.getUser().getId(), userId)) {
            throw new IllegalArgumentException("Subscription does not belong to user");
        }

        subscriptionRepository.delete(subscription);
    }

    @Override
    public List<SubscriptionDto> getTop3Subscriptions() {
        List<Subscription> all = subscriptionRepository.findAll();

        return all.stream()
                .collect(Collectors.groupingBy(
                        Subscription::getServiceName, Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .map(name -> SubscriptionDto.builder().serviceName(name).build())
                .collect(Collectors.toList());
    }
}