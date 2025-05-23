package com.example.subscriptions;

import org.springframework.boot.SpringApplication;

public class TestSubscriptionsApplication {

	public static void main(String[] args) {
		SpringApplication.from(SubscriptionsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
