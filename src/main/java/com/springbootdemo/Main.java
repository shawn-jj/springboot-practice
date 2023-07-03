package com.springbootdemo;

import com.springbootdemo.customer.Customer;
import com.springbootdemo.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        return args -> {
            Customer a = new Customer("a", "a@123.com", 20);
            Customer j = new Customer("j", "j@123.com", 21);
            List<Customer> customers = List.of(a, j);
            customerRepository.saveAll(customers);
        };
    }

}
