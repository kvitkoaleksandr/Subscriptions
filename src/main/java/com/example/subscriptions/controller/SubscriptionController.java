package com.example.subscriptions.controller;

import com.example.subscriptions.dto.*;
import com.example.subscriptions.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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