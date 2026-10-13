package com.cinema.reservaitionservice.service;


import com.cinema.reservaitionservice.dto.CustomerRequest;
import com.cinema.reservaitionservice.exception.ResourceAlreadyFoundException;
import com.cinema.reservaitionservice.exception.ResourceNotFoundException;
import com.cinema.reservaitionservice.model.Customer;
import com.cinema.reservaitionservice.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException("Customer not found with id " + id));
    }

    public Customer createCustomer(CustomerRequest request) {
        if(customerRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new ResourceAlreadyFoundException("Customer with email " + request.getEmail() + " already exists");
        }
        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());

        return customerRepository.save(customer);

    }

    public Customer updateCustomer(Integer id, CustomerRequest request) {

        Customer customer = getCustomerById(id);

        if(!customer.getEmail()
                .equals(request.getEmail())
                && customerRepository
                .existsByEmailIgnoreCase(request.getEmail())) {
            throw new ResourceAlreadyFoundException("Customer with email " + request.getEmail() + " already exists");

        }
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Integer id) {

        Customer customer = getCustomerById(id);

        customerRepository.delete(customer);
    }
}
