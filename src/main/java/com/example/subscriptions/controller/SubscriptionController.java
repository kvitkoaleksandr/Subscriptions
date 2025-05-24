package com.example.subscriptions.controller;

import com.example.subscriptions.dto.CreateSubscriptionRequest;
import com.example.subscriptions.dto.SubscriptionDto;
import com.example.subscriptions.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing user subscriptions.
 * Provides endpoints for adding, listing, and deleting subscriptions.
 */
@RestController
@RequestMapping("/users/{userId}/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionDto> add(
            @PathVariable Long userId,
            @RequestBody @Valid CreateSubscriptionRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.addSubscription(userId, request));
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionDto>> getAll(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionService.getUserSubscriptions(userId));
    }

    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long userId,
            @PathVariable Long subscriptionId
    ) {
        subscriptionService.deleteSubscription(userId, subscriptionId);
        return ResponseEntity.noContent().build();
    }
}