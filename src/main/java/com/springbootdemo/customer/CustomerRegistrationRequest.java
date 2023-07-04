package com.springbootdemo.customer;

import java.security.SecureRandom;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
) {
}
