package com.springbootdemo.journey;

import com.springbootdemo.customer.Customer;
import com.springbootdemo.customer.CustomerRegistrationRequest;
import com.springbootdemo.customer.CustomerUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest  {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void canRegisterCustomer() {
        // create registration request
        Random random = new Random();
        String name = "_name_";
        String email = "_email@email.com_" + random.nextInt();
        int age = random.nextInt(1, 100);
        String customerURI = "/api/v1/customers";

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);

        // send a post request
        webTestClient.post()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() { })
                .returnResult()
                .getResponseBody();

        // make sure customer is present
        Customer expectCustomer = new Customer(name, email, age);

        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectCustomer);

        // get customer by id
        int id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        expectCustomer.setId(id);

        webTestClient.get()
                .uri(customerURI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() { })
                .isEqualTo(expectCustomer);
    }

    @Test
    void canDeleteCustomer() {
        // create registration request
        Random random = new Random();
        String name = "_name_";
        String email = "_email@email.com_" + random.nextInt();
        int age = random.nextInt(1, 100);
        String customerURI = "/api/v1/customers";

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);

        // send a post request
        webTestClient.post()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() { })
                .returnResult()
                .getResponseBody();

        // get customer by id
        int id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // delete customer
        webTestClient.delete()
                .uri(customerURI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(customerURI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        // create registration request
        Random random = new Random();
        String name = "_name_";
        String email = "_email@email.com_" + random.nextInt();
        int age = random.nextInt(1, 100);
        String customerURI = "/api/v1/customers";

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);

        // send a post request
        webTestClient.post()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() { })
                .returnResult()
                .getResponseBody();

        // get customer by id
        int id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // update customer
        String newName = "_newName_";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(newName, null, null);

        webTestClient.put()
                .uri(customerURI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        Customer updateCustomer = webTestClient.get()
                .uri(customerURI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult().
                getResponseBody();

        Customer expected = new Customer(id, newName, email, age);

        assertThat(updateCustomer).isEqualTo(expected);
    }
}
