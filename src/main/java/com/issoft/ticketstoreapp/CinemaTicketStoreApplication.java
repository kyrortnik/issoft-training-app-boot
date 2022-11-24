package com.issoft.ticketstoreapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CinemaTicketStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaTicketStoreApplication.class, args);
    }
}