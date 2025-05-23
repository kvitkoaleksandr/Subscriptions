package com.example.subscriptions.service.impl;

import com.example.subscriptions.dto.CreateSubscriptionRequest;
import com.example.subscriptions.dto.SubscriptionDto;
import com.example.subscriptions.entity.Subscription;
import com.example.subscriptions.entity.User;
import com.example.subscriptions.exception.ResourceNotFoundException;
import com.example.subscriptions.repository.SubscriptionRepository;
import com.example.subscriptions.repository.UserRepository;
import com.example.subscriptions.service.SubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private UserRepository userRepository;

    private SubscriptionService subscriptionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subscriptionService = new SubscriptionServiceImpl(subscriptionRepository, userRepository);
    }

    @Test
    void shouldAddSubscriptionForUser() {
        User user = User.builder().id(1L).build();
        CreateSubscriptionRequest request = CreateSubscriptionRequest.builder()
                .serviceName("Netflix")
                .build();
        Subscription saved = Subscription.builder().id(100L).serviceName("Netflix").user(user).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(saved);

        SubscriptionDto result = subscriptionService.addSubscription(1L, request);

        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getServiceName()).isEqualTo("Netflix");
    }

    @Test
    void shouldReturnUserSubscriptions() {
        Subscription sub = Subscription.builder().id(1L).serviceName("Netflix").build();
        when(userRepository.existsById(1L)).thenReturn(true);
        when(subscriptionRepository.findByUserId(1L)).thenReturn(List.of(sub));

        List<SubscriptionDto> result = subscriptionService.getUserSubscriptions(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getServiceName()).isEqualTo("Netflix");
    }

    @Test
    void shouldThrowWhenUserNotFoundForSubscriptionQuery() {
        when(userRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> subscriptionService.getUserSubscriptions(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void shouldDeleteUserSubscription() {
        User user = User.builder().id(1L).build();
        Subscription sub = Subscription.builder().id(10L).serviceName("Netflix").user(user).build();

        when(subscriptionRepository.findById(10L)).thenReturn(Optional.of(sub));

        subscriptionService.deleteSubscription(1L, 10L);

        verify(subscriptionRepository).delete(sub);
    }

    @Test
    void shouldThrowIfSubscriptionDoesNotBelongToUser() {
        User user1 = User.builder().id(1L).build();
        User user2 = User.builder().id(2L).build();
        Subscription sub = Subscription.builder().id(10L).serviceName("Spotify").user(user2).build();

        when(subscriptionRepository.findById(10L)).thenReturn(Optional.of(sub));

        assertThatThrownBy(() -> subscriptionService.deleteSubscription(1L, 10L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Subscription does not belong to user");
    }

    @Test
    void shouldReturnTop3Subscriptions() {
        List<Subscription> allSubs = List.of(
                new Subscription(1L, "Netflix", null),
                new Subscription(2L, "Netflix", null),
                new Subscription(3L, "YouTube", null),
                new Subscription(4L, "YouTube", null),
                new Subscription(5L, "YouTube", null),
                new Subscription(6L, "Spotify", null),
                new Subscription(7L, "Spotify", null)
        );

        when(subscriptionRepository.findAll()).thenReturn(allSubs);

        List<SubscriptionDto> top = subscriptionService.getTop3Subscriptions();

        assertThat(top).hasSize(3);
        assertThat(top.get(0).getServiceName()).isEqualTo("YouTube");
        assertThat(top.get(1).getServiceName()).isEqualTo("Netflix");
        assertThat(top.get(2).getServiceName()).isEqualTo("Spotify");
    }
}