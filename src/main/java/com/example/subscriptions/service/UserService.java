package com.example.subscriptions.service;

import com.example.subscriptions.dto.*;

import java.util.List;

/**
 * Service interface for user-related operations.
 */
public interface UserService {

    /**
     * Creates a new user.
     *
     * @param request the user creation request
     * @return created user DTO
     */
    UserDto createUser(CreateUserRequest request);

    /**
     * Retrieves a user by ID.
     *
     * @param id the user ID
     * @return user DTO
     */
    UserDto getUserById(Long id);

    /**
     * Updates an existing user.
     *
     * @param id the user ID
     * @param request updated user data
     * @return updated user DTO
     */
    UserDto updateUser(Long id, CreateUserRequest request);

    /**
     * Deletes a user by ID.
     *
     * @param id the user ID
     */
    void deleteUser(Long id);

    /**
     * Retrieves all users.
     *
     * @return list of user DTOs
     */
    List<UserDto> getAllUsers();
}