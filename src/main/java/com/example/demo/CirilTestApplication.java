package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CirilTestApplication {
	@Autowired
	public static void main(String[] args) {
		SpringApplication.run(CirilTestApplication.class, args);
	}
	 

}
