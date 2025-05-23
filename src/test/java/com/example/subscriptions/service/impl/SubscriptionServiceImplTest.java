package com.example.subscriptions.service.impl;

import com.example.subscriptions.dto.*;
import com.example.subscriptions.entity.*;
import com.example.subscriptions.exception.ResourceNotFoundException;
import com.example.subscriptions.repository.SubscriptionRepository;
import com.example.subscriptions.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionServiceImplTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private UserRepository userRepository;

    private SubscriptionServiceImpl subscriptionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subscriptionService = new SubscriptionServiceImpl(subscriptionRepository, userRepository);
    }

    @Test
    void shouldAddSubscriptionForUser() {
        User user = User.builder().id(1L).build();
        CreateSubscriptionRequest request = new CreateSubscriptionRequest("Netflix");
        Subscription saved = Subscription.builder().id(100L).serviceName("Netflix").user(user).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(saved);

        SubscriptionDto result = subscriptionService.addSubscription(1L, request);

        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getServiceName()).isEqualTo("Netflix");
    }

    @Test
    void shouldThrowWhenAddingSubscriptionToUnknownUser() {
        CreateSubscriptionRequest request = new CreateSubscriptionRequest("Netflix");
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> subscriptionService.addSubscription(999L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void shouldReturnUserSubscriptions() {
        Subscription s1 = new Subscription(1L, "Netflix", null);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(subscriptionRepository.findByUserId(1L)).thenReturn(List.of(s1));

        List<SubscriptionDto> result = subscriptionService.getUserSubscriptions(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getServiceName()).isEqualTo("Netflix");
    }

    @Test
    void shouldThrowWhenFetchingSubscriptionsOfUnknownUser() {
        when(userRepository.existsById(2L)).thenReturn(false);

        assertThatThrownBy(() -> subscriptionService.getUserSubscriptions(2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void shouldDeleteUserSubscription() {
        User user = User.builder().id(1L).build();
        Subscription sub = Subscription.builder().id(11L).serviceName("YouTube").user(user).build();

        when(subscriptionRepository.findById(11L)).thenReturn(Optional.of(sub));

        subscriptionService.deleteSubscription(1L, 11L);

        verify(subscriptionRepository).delete(sub);
    }

    @Test
    void shouldThrowWhenDeletingSubscriptionThatDoesNotBelongToUser() {
        User userA = User.builder().id(1L).build();
        User userB = User.builder().id(2L).build();
        Subscription sub = Subscription.builder().id(15L).serviceName("Spotify").user(userB).build();

        when(subscriptionRepository.findById(15L)).thenReturn(Optional.of(sub));

        assertThatThrownBy(() -> subscriptionService.deleteSubscription(1L, 15L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Subscription does not belong to user");
    }

    @Test
    void shouldThrowWhenDeletingUnknownSubscription() {
        when(subscriptionRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> subscriptionService.deleteSubscription(1L, 404L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Subscription not found");
    }

    @Test
    void shouldReturnTop3Subscriptions() {
        List<Subscription> allSubs = List.of(
                new Subscription(1L, "Netflix", null),
                new Subscription(2L, "Netflix", null),
                new Subscription(3L, "Spotify", null),
                new Subscription(4L, "YouTube", null),
                new Subscription(5L, "YouTube", null),
                new Subscription(6L, "YouTube", null)
        );

        when(subscriptionRepository.findAll()).thenReturn(allSubs);

        List<SubscriptionDto> top = subscriptionService.getTop3Subscriptions();

        assertThat(top).hasSize(3);
        assertThat(top.get(0).getServiceName()).isEqualTo("YouTube");
        assertThat(top.get(1).getServiceName()).isEqualTo("Netflix");
        assertThat(top.get(2).getServiceName()).isEqualTo("Spotify");
    }
}