package com.cinema.movieservice.repository;

import com.cinema.movieservice.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening,Integer> {
    List<Screening> findByAuditorium_IdAndActiveTrue(Integer auditoriumId);

    List<Screening> findByMovie_Id(Integer movieId);
}
