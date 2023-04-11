package ru.didenko.smartconsulting.seasonalservices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SeasonalServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeasonalServicesApplication.class, args);
        log.info("Server started on http://localhost:9090/swagger-ui/index.html#/");
    }

}
