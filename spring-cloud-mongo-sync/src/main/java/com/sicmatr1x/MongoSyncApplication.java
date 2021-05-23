package com.sicmatr1x;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MongoSyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(MongoSyncApplication.class, args);
	}
}
