package com.creatorshield;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class CreatorShieldApplication {

    public static void main(String[] args) {
        SpringApplication.run(CreatorShieldApplication.class, args);
    }
}
