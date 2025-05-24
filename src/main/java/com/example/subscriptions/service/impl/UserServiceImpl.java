package com.example.subscriptions.service.impl;

import com.example.subscriptions.dto.CreateUserRequest;
import com.example.subscriptions.dto.UserDto;
import com.example.subscriptions.entity.User;
import com.example.subscriptions.exception.ResourceNotFoundException;
import com.example.subscriptions.kafka.UserEventProducer;
import com.example.subscriptions.mapper.UserMapper;
import com.example.subscriptions.repository.UserRepository;
import com.example.subscriptions.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link UserService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserEventProducer userEventProducer;

    @Override
    public UserDto createUser(CreateUserRequest request) {
        log.info("Creating user with email: {}", request.getEmail());
        User user = UserMapper.toEntity(request);
        User saved = userRepository.save(user);
        UserDto dto = UserMapper.toDto(saved);

        String kafkaMessage = "User created: id=" + dto.getId() + ", email=" + dto.getEmail();
        userEventProducer.sendUserCreatedEvent(kafkaMessage);

        return dto;
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public UserDto getUserById(Long id) {
        try {
            log.debug("Fetching user with ID: {}", id);
            User user = userRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("User not found with ID: {}", id);
                        return new ResourceNotFoundException("User not found");
                    });
            return UserMapper.toDto(user);
        } catch (Exception e) {
            log.error("Ошибка при получении пользователя по ID из Redis или БД", e);
            throw e; // обязательно пробрось дальше, иначе вернётся 200
        }
    }

    @Override
    public UserDto updateUser(Long id, CreateUserRequest request) {
        log.info("Updating user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cannot update. User not found with ID: {}", id);
                    return new ResourceNotFoundException("User not found");
                });

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        User updated = userRepository.save(user);
        log.debug("User updated: ID={}, new name={}, new email={}", id, updated.getName(), updated.getEmail());
        return UserMapper.toDto(updated);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            log.warn("Cannot delete. User not found with ID: {}", id);
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
        log.debug("User deleted with ID: {}", id);
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.debug("Fetching all users");
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }
}