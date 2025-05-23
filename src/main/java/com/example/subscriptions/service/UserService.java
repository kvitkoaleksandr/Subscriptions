package com.example.subscriptions.service;

import com.example.subscriptions.dto.*;

import java.util.List;

public interface UserService {

    UserDto createUser(CreateUserRequest request);

    UserDto getUserById(Long id);

    UserDto updateUser(Long id, CreateUserRequest request);

    void deleteUser(Long id);

    List<UserDto> getAllUsers();
}