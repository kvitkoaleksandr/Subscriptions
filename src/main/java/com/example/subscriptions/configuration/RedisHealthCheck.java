package com.example.subscriptions.configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisHealthCheck {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void checkRedis() {
        try {
            redisTemplate.opsForValue().set("ping", "pong");
            log.info("Redis доступен. Ключ 'ping' установлен.");
        } catch (Exception e) {
            log.error("Redis не доступен: " + e.getMessage(), e);
        }
    }
}