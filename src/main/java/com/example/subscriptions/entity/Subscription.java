package com.example.subscriptions.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a subscription to a service by a user.
 */
@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String serviceName;

    /**
     * The user who owns this subscription.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}