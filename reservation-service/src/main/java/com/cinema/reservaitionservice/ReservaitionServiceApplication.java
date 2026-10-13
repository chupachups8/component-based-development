package com.cinema.reservaitionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ReservaitionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservaitionServiceApplication.class, args);
    }

}
