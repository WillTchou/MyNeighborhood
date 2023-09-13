package com.example.project.MyNeighborhood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyNeighborhoodApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyNeighborhoodApplication.class, args);
	}

}
