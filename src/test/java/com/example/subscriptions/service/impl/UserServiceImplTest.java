package com.example.subscriptions.service.impl;

import com.example.subscriptions.dto.CreateUserRequest;
import com.example.subscriptions.dto.UserDto;
import com.example.subscriptions.entity.User;
import com.example.subscriptions.exception.ResourceNotFoundException;
import com.example.subscriptions.mapper.UserMapper;
import com.example.subscriptions.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void shouldCreateUser() {
        CreateUserRequest request = new CreateUserRequest("John Doe", "john@example.com");
        User user = UserMapper.toEntity(request);
        user.setId(1L);

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto result = userService.createUser(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("john@example.com");

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldGetUserById() {
        User user = User.builder().id(1L).name("Test User").email("test@mail.com").build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test User");
        verify(userRepository).findById(1L);
    }

    @Test
    void shouldThrowIfUserNotFoundById() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository).findById(999L);
    }

    @Test
    void shouldUpdateUser() {
        User existing = User.builder().id(1L).name("Old").email("old@example.com").build();
        CreateUserRequest update = new CreateUserRequest("New", "new@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        UserDto result = userService.updateUser(1L, update);

        assertThat(result.getName()).isEqualTo("New");
        assertThat(result.getEmail()).isEqualTo("new@example.com");
    }

    @Test
    void shouldThrowWhenUpdatingNonexistentUser() {
        when(userRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(404L, new CreateUserRequest("Test", "t@test.com")))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void shouldDeleteUserIfExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonexistentUser() {
        when(userRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void shouldReturnAllUsers() {
        List<User> users = List.of(User.builder().id(1L).name("Alice").email("a@mail.com").build());

        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> result = userService.getAllUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Alice");
    }
}