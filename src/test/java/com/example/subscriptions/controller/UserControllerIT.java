package com.example.subscriptions.controller;

import com.example.subscriptions.dto.CreateUserRequest;
import com.example.subscriptions.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    private String baseUrl() {
        return "http://localhost:" + port + "/users";
    }

    @Test
    void shouldCreateAndGetUser() {
        CreateUserRequest request = new CreateUserRequest("Integration Test", "integration@test.com");

        ResponseEntity<UserDto> response = rest.postForEntity(baseUrl(), request, UserDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();

        Long userId = response.getBody().getId();
        ResponseEntity<UserDto> getResponse = rest.getForEntity(baseUrl() + "/" + userId, UserDto.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getEmail()).isEqualTo("integration@test.com");
    }
}