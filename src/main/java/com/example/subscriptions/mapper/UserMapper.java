package com.example.subscriptions.mapper;

import com.example.subscriptions.dto.*;
import com.example.subscriptions.entity.*;

import java.util.stream.Collectors;

/**
 * Utility class for mapping between {@link User} entities and {@link UserDto} objects.
 */
public class UserMapper {

    /**
     * Converts a {@link User} entity to a {@link UserDto}.
     *
     * @param user the user entity
     * @return the user DTO
     */
    public static UserDto toDto(User user) {
        if (user == null) return null;

        try {
            return UserDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .subscriptions(
                            user.getSubscriptions() != null ?
                                    user.getSubscriptions().stream()
                                            .map(SubscriptionMapper::toDto)
                                            .collect(Collectors.toList())
                                    : null
                    )
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Converts a {@link CreateUserRequest} to a {@link User} entity.
     *
     * @param request the user creation request
     * @return the user entity
     */
    public static User toEntity(CreateUserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();
    }
}