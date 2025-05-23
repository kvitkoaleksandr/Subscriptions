package com.example.subscriptions.dto;

import lombok.*;

/**
 * DTO representing a user's subscription to a service.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionDto {
    private Long id;
    private String serviceName;
}