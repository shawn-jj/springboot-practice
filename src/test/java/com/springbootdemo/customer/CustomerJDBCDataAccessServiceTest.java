package com.springbootdemo.customer;

import com.springbootdemo.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.Random;


class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(getJdbcTemplate(), customerRowMapper);
    }

    @Test
    void selectAllCustomers() {
        //Given
        Random random = new Random();
        String email = "_email@email.com_" + random.nextInt();
        Customer customer = new Customer("_name_", email, 20);
        underTest.insertCustomer(customer);

        //When
        List<Customer> actual = underTest.selectAllCustomers();

        //Then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        //Given
        Random random = new Random();
        String email = "_email@email.com_" + random.nextInt();
        Customer customer = new Customer("_name_", email, 20);
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        // Given
        int id = -1;

        // When
        var actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isEmpty();
    }

//    @Test
//    void insertCustomer() {
//    }

    @Test
    void existPersonWithEmail() {
        //Given
        Random random = new Random();
        String email = "_email@email.com_" + random.nextInt();
        Customer customer = new Customer("_name_", email, 20);
        underTest.insertCustomer(customer);

        // When
        boolean actual = underTest.existPersonWithEmail(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existPersonWithEmailReturnsFalseWhenDoesNotExists() {
        //Given
        Random random = new Random();
        String email = "_email@email.com_" + random.nextInt();

        // When
        boolean actual = underTest.existPersonWithEmail(email);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void existPersonWithId() {
        //Given
        Random random = new Random();
        String email = "_email@email.com_" + random.nextInt();
        Customer customer = new Customer("_name_", email, 20);
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        var actual = underTest.existPersonWithId(id);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existCustomerWithIdReturnsFalseWhenDoesNotExists() {
        // Given
        int id = -1;

        // When
        var actual = underTest.existPersonWithId(id);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerById() {
        //Given
        Random random = new Random();
        String email = "_email@email.com_" + random.nextInt();
        Customer customer = new Customer("_name_", email, 20);
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        underTest.deleteCustomerById(id);

        // Then
        var actual = underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void updateCustomerName() {
        //Given
        Random random = new Random();
        String email = "_email@email.com_" + random.nextInt();
        Customer customer = new Customer("_name_", email, 20);
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        String newName = "foo";

        // When the name is changed
        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);

        underTest.updateCustomer(update);

        // Then
        var actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName); // change
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerEmail() {
        //Given
        Random random = new Random();
        String email = "_email@email.com_" + random.nextInt();
        Customer customer = new Customer("_name_", email, 20);
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        String newEmail = random.nextInt() + "_email@email.com_" + random.nextInt();

        // When the email is changed
        Customer update = new Customer();
        update.setId(id);
        update.setEmail(newEmail);

        underTest.updateCustomer(update);

        // Then
        var actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(newEmail); // change
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerAge() {
        //Given
        Random random = new Random();
        String email = "_email@email.com_" + random.nextInt();
        Customer customer = new Customer("_name_", email, 20);
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        int newAge = 100;

        // When the age is changed
        Customer update = new Customer();
        update.setId(id);
        update.setAge(newAge);

        underTest.updateCustomer(update);

        // Then
        var actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(newAge); // change
        });
    }

    @Test
    void updateCustomerAllProperty() {
        //Given
        Random random = new Random();
        String email = "_email@email.com_" + random.nextInt();
        Customer customer = new Customer("_name_", email, 20);
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When every property is changed
        Customer update = new Customer();
        update.setId(id);
        update.setName("newName");
        update.setEmail("_newEmail@email.com_" + random.nextInt());
        update.setAge(100);

        underTest.updateCustomer(update);

        // Then
        var actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValue(update);
    }

    @Test
    void willNotUpdateCustomerWhenNothingChanged() {
        //Given
        Random random = new Random();
        String email = "_email@email.com_" + random.nextInt();
        Customer customer = new Customer("_name_", email, 20);
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When update with no change
        Customer update = new Customer();
        update.setId(id);

        underTest.updateCustomer(update);

        // Then
        var actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }
}