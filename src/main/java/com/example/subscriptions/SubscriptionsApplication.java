package com.example.subscriptions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SubscriptionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubscriptionsApplication.class, args);
	}
}