package com.example.subscriptions.controller;

import com.example.subscriptions.dto.SubscriptionDto;
import com.example.subscriptions.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for retrieving the most popular subscription services.
 */
@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class TopSubscriptionsController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/top")
    public ResponseEntity<List<SubscriptionDto>> getTop3() {
        return ResponseEntity.ok(subscriptionService.getTop3Subscriptions());
    }
}