package com.example.subscriptions.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entity representing an application user.
 * Each user can have multiple subscriptions.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    /**
     * Primary key. Automatically generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    /**
     * List of subscriptions linked to the user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subscription> subscriptions;
}