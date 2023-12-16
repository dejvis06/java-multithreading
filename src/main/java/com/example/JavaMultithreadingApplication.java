package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaMultithreadingApplication {

	public static void main(String[] args) {
		System.err.println("Main thread name: " + Thread.currentThread().getName());
		SpringApplication.run(JavaMultithreadingApplication.class, args);
	}

}
