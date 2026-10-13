package com.cinema.movieservice.repository;

import com.cinema.movieservice.model.Auditorium;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditoriumRepository extends JpaRepository<Auditorium, Integer> {
    boolean existsByNameIgnoreCase(String name);
}
