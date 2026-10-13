package com.cinema.movieservice.service;


import com.cinema.movieservice.dto.ScreeningRequest;
import com.cinema.movieservice.exception.ResourceNotFoundException;
import com.cinema.movieservice.exception.ScreeningConflictException;
import com.cinema.movieservice.model.Auditorium;
import com.cinema.movieservice.model.Movie;
import com.cinema.movieservice.model.Screening;
import com.cinema.movieservice.repository.AuditoriumRepository;
import com.cinema.movieservice.repository.MovieRepository;
import com.cinema.movieservice.repository.ScreeningRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScreeningService {
    AuditoriumRepository  auditoriumRepository;
    MovieRepository movieRepository;
    ScreeningRepository screeningRepository;

    public ScreeningService( AuditoriumRepository auditoriumRepository, MovieRepository movieRepository, ScreeningRepository screeningRepository) {
        this.auditoriumRepository = auditoriumRepository;
        this.movieRepository = movieRepository;
        this.screeningRepository = screeningRepository;
    }

    public List<Screening> getAllScreenings() {
        return screeningRepository.findAll();
    }

    public Screening getScreeningById(Integer id) {
        return screeningRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException("Screening with given id not found.")
                );
    }

    public List<Screening> getScreeningsByMovieId(Integer movieId){
        if(!movieRepository.existsById(movieId)){
            throw new ResourceNotFoundException("Movie with given id not found.");
        }
        return screeningRepository.findByMovie_Id(movieId);

    }

    public Screening createScreening(ScreeningRequest request){
        Movie movie=movieRepository
                .findById(request.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie with given id not found."));

        Auditorium auditorium = auditoriumRepository
                .findById(request.getAuditoriumId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Auditorium with id "
                                        + request.getAuditoriumId()
                                        + " not found"
                        )
                );

        checkForTimeConflict(
                auditorium.getId(),
                movie,
                request.getStartTime(),
                null
        );

        Screening screening = new Screening();

        screening.setMovie(movie);
        screening.setAuditorium(auditorium);
        screening.setStartTime(request.getStartTime());
        screening.setTicketPrice(request.getTicketPrice());

        if (request.getActive() != null) {
            screening.setActive(request.getActive());
        } else {
            screening.setActive(true);
        }

        return screeningRepository.save(screening);
    }

    public Screening updateScreening(
            Integer id,
            ScreeningRequest request) {

        Screening screening = getScreeningById(id);

        Movie movie = movieRepository
                .findById(request.getMovieId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Movie with id "
                                        + request.getMovieId()
                                        + " not found"
                        )
                );

        Auditorium auditorium = auditoriumRepository
                .findById(request.getAuditoriumId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Auditorium with id "
                                        + request.getAuditoriumId()
                                        + " not found"
                        )
                );

        checkForTimeConflict(
                auditorium.getId(),
                movie,
                request.getStartTime(),
                id
        );

        screening.setMovie(movie);
        screening.setAuditorium(auditorium);
        screening.setStartTime(request.getStartTime());
        screening.setTicketPrice(request.getTicketPrice());

        if (request.getActive() != null) {
            screening.setActive(request.getActive());
        }

        return screeningRepository.save(screening);
    }

    public void deleteScreening(Integer id) {

        Screening screening = getScreeningById(id);

        screeningRepository.delete(screening);
    }

    private void checkForTimeConflict(
            Integer auditoriumId,
            Movie newMovie,
            LocalDateTime newStart,
            Integer screeningIdToIgnore) {

        LocalDateTime newEnd =
                newStart.plusMinutes(newMovie.getDuration());

        List<Screening> existingScreenings =
                screeningRepository
                        .findByAuditorium_IdAndActiveTrue(auditoriumId);

        for (Screening existing : existingScreenings) {

            if (existing.getId().equals(screeningIdToIgnore)) {
                continue;
            }

            LocalDateTime existingStart =
                    existing.getStartTime();

            LocalDateTime existingEnd =
                    existingStart.plusMinutes(
                            existing.getMovie().getDuration()
                    );

            boolean overlaps =
                    newStart.isBefore(existingEnd)
                            && newEnd.isAfter(existingStart);

            if (overlaps) {
                throw new ScreeningConflictException(
                        "Auditorium is already occupied in the selected period"
                );
            }
        }
    }




}
