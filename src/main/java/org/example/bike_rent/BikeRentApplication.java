package org.example.bike_rent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "org.example.bike_rent.entity")
@EnableJpaRepositories("org.example.bike_rent.repository")
public class BikeRentApplication {

    public static void main(String[] args) {
        SpringApplication.run(BikeRentApplication.class, args);
    }

}
