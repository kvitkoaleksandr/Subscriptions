package com.example.subscriptions.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * DTO representing a user along with their subscriptions.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto implements Serializable {
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