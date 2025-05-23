package com.example.subscriptions.controller;

import com.example.subscriptions.dto.SubscriptionDto;
import com.example.subscriptions.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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