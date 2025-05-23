package com.example.subscriptions.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO used for creating a new subscription for a user.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSubscriptionRequest {

    @NotBlank(message = "Service name is mandatory")
    private String serviceName;
}