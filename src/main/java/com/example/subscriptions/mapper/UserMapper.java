package com.example.subscriptions.mapper;

import com.example.subscriptions.dto.*;
import com.example.subscriptions.entity.*;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserDto toDto(User user) {
        if (user == null) return null;

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
    }

    public static User toEntity(CreateUserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();
    }
}