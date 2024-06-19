package com.communityhub.community.hub_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CommunityHubAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommunityHubAppApplication.class, args);
	}

}
