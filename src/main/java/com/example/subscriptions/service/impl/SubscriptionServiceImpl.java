package com.example.subscriptions.service.impl;

import com.example.subscriptions.dto.CreateSubscriptionRequest;
import com.example.subscriptions.dto.SubscriptionDto;
import com.example.subscriptions.entity.Subscription;
import com.example.subscriptions.entity.User;
import com.example.subscriptions.exception.ResourceNotFoundException;
import com.example.subscriptions.mapper.SubscriptionMapper;
import com.example.subscriptions.repository.SubscriptionRepository;
import com.example.subscriptions.repository.UserRepository;
import com.example.subscriptions.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link SubscriptionService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Override
    public SubscriptionDto addSubscription(Long userId, CreateSubscriptionRequest request) {
        log.info("Adding subscription '{}' for user with ID {}", request.getServiceName(), userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Cannot add subscription. User not found with ID: {}", userId);
                    return new ResourceNotFoundException("User not found");
                });

        Subscription subscription = SubscriptionMapper.toEntity(request, user);
        Subscription saved = subscriptionRepository.save(subscription);
        log.debug("Subscription created with ID: {}", saved.getId());
        return SubscriptionMapper.toDto(saved);
    }

    @Override
    public List<SubscriptionDto> getUserSubscriptions(Long userId) {
        log.debug("Fetching subscriptions for user with ID: {}", userId);
        if (!userRepository.existsById(userId)) {
            log.warn("User not found while fetching subscriptions. ID: {}", userId);
            throw new ResourceNotFoundException("User not found");
        }

        return subscriptionRepository.findByUserId(userId).stream()
                .map(SubscriptionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSubscription(Long userId, Long subscriptionId) {
        log.info("Deleting subscription with ID {} for user {}", subscriptionId, userId);
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> {
                    log.warn("Subscription not found with ID: {}", subscriptionId);
                    return new ResourceNotFoundException("Subscription not found");
                });

        if (!Objects.equals(subscription.getUser().getId(), userId)) {
            log.warn("User {} attempted to delete subscription {} that does not belong to them", userId, subscriptionId);
            throw new IllegalArgumentException("Subscription does not belong to user");
        }

        subscriptionRepository.delete(subscription);
        log.debug("Subscription {} deleted for user {}", subscriptionId, userId);
    }

    @Override
    public List<SubscriptionDto> getTop3Subscriptions() {
        log.debug("Calculating top 3 subscriptions");
        List<Subscription> all = subscriptionRepository.findAll();

        List<SubscriptionDto> top = all.stream()
                .collect(Collectors.groupingBy(
                        Subscription::getServiceName, Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .map(name -> SubscriptionDto.builder().serviceName(name).build())
                .collect(Collectors.toList());

        log.info("Top 3 subscriptions: {}", top.stream().map(SubscriptionDto::getServiceName).toList());
        return top;
    }
}