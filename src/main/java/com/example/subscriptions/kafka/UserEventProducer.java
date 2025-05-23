package com.example.subscriptions.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka producer for user-related events.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "users.created";

    public void sendUserCreatedEvent(String message) {
        kafkaTemplate.send(TOPIC, message);
        log.info("Sent user created event to Kafka: {}", message);
    }
}