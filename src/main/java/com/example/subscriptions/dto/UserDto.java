package com.example.subscriptions.dto;

import lombok.*;

import java.util.List;

/**
 * DTO representing a user along with their subscriptions.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    /**
     * Unique identifier of the user.
     */
    private Long id;
    private String name;
    private String email;
    /**
     * List of subscriptions associated with the user.
     */
    private List<SubscriptionDto> subscriptions;
}