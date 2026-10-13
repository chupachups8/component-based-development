package com.cinema.reservaitionservice.repository;

import com.cinema.reservaitionservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    boolean existsByEmailIgnoreCase(String email);
}
