package com.example.subscriptions.controller;

import com.example.subscriptions.dto.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SubscriptionControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    private Long createTestUser(String name, String email) {
        CreateUserRequest request = new CreateUserRequest(name, email);
        ResponseEntity<UserDto> response = rest.postForEntity(baseUrl() + "/users", request, UserDto.class);
        return response.getBody().getId();
    }

    @Test
    void shouldCreateAndGetAndDeleteSubscription() {
        Long userId = createTestUser("Sub Tester", "sub@test.com");

        CreateSubscriptionRequest subReq = new CreateSubscriptionRequest("Netflix");
        ResponseEntity<SubscriptionDto> postResp = rest.postForEntity(
                baseUrl() + "/users/" + userId + "/subscriptions", subReq, SubscriptionDto.class);
        assertThat(postResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(postResp.getBody().getServiceName()).isEqualTo("Netflix");
        Long subId = postResp.getBody().getId();

        ResponseEntity<SubscriptionDto[]> getResp = rest.getForEntity(
                baseUrl() + "/users/" + userId + "/subscriptions", SubscriptionDto[].class);
        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResp.getBody()).hasSize(1);
        assertThat(getResp.getBody()[0].getServiceName()).isEqualTo("Netflix");

        rest.delete(baseUrl() + "/users/" + userId + "/subscriptions/" + subId);

        ResponseEntity<SubscriptionDto[]> afterDelete = rest.getForEntity(
                baseUrl() + "/users/" + userId + "/subscriptions", SubscriptionDto[].class);
        assertThat(afterDelete.getBody()).isEmpty();
    }

    @Test
    void shouldReturnTop3Subscriptions() {
        Long userA = createTestUser("A", "a@t.com");
        Long userB = createTestUser("B", "b@t.com");
        Long userC = createTestUser("C", "c@t.com");

        List<String> netflix = List.of("Netflix", "Netflix");
        List<String> youtube = List.of("YouTube", "YouTube", "YouTube");
        List<String> spotify = List.of("Spotify");

        List.of(userA, userB).forEach(uid ->
                netflix.forEach(name -> rest.postForEntity(
                        baseUrl() + "/users/" + uid + "/subscriptions",
                        new CreateSubscriptionRequest(name),
                        SubscriptionDto.class)));

        List.of(userA, userB, userC).forEach(uid ->
                youtube.forEach(name -> rest.postForEntity(
                        baseUrl() + "/users/" + uid + "/subscriptions",
                        new CreateSubscriptionRequest(name),
                        SubscriptionDto.class)));

        rest.postForEntity(baseUrl() + "/users/" + userC + "/subscriptions",
                new CreateSubscriptionRequest("Spotify"),
                SubscriptionDto.class);

        ResponseEntity<SubscriptionDto[]> topResp =
                rest.getForEntity(baseUrl() + "/subscriptions/top", SubscriptionDto[].class);

        assertThat(topResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<String> top = Arrays.stream(topResp.getBody())
                .map(SubscriptionDto::getServiceName).toList();

        assertThat(top).containsExactly("YouTube", "Netflix", "Spotify");
    }
}