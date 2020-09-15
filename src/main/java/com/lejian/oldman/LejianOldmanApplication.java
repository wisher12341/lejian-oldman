package com.lejian.oldman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LejianOldmanApplication {

	public static void main(String[] args) {
		SpringApplication.run(LejianOldmanApplication.class, args);
	}

}
