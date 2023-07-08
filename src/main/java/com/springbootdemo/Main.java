package com.springbootdemo;

import com.github.javafaker.Faker;
import com.springbootdemo.customer.Customer;
import com.springbootdemo.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        return args -> {
            Faker faker = new Faker();
            Random random = new Random();
            var name = faker.name();
            String firstName = name.firstName().toLowerCase();
            String lastName = name.lastName().toLowerCase();
            Customer customer = new Customer(
                    firstName + " " + lastName,
                    firstName + "." + lastName + "@example.com",
                    random.nextInt(16, 99)
            );
//            customerRepository.save(customer);
        };
    }

}
