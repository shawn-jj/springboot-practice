package com.springbootdemo.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerDataAccessService implements CustomerDao{

    //db
    private static List<Customer> customers;

    static {
        customers = new ArrayList<>();
        Customer a = new Customer(1, "a", "a@123.com", 20);
        customers.add(a);
        Customer j = new Customer(2, "j", "j@123.com", 21);
        customers.add(j);
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }
}
