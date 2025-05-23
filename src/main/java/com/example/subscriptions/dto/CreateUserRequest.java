package com.example.subscriptions.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO used for creating or updating a user.
 * Includes basic validation for name and email.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {

    /**
     * The user's full name.
     */
    @NotBlank(message = "Name is mandatory")
    private String name;

    /**
     * The user's email address. Must be a valid email format.
     */
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;
}