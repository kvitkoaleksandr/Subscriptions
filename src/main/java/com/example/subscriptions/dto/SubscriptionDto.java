package com.example.subscriptions.dto;

import lombok.*;

import java.io.Serializable;

/**
 * DTO representing a user's subscription to a service.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionDto implements Serializable {
    private Long id;
    private String serviceName;
}