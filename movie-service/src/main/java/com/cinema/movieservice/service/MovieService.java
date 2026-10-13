package com.cinema.movieservice.service;

import com.cinema.movieservice.dto.MovieRequest;
import com.cinema.movieservice.exception.ResourceNotFoundException;
import com.cinema.movieservice.model.Movie;
import com.cinema.movieservice.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(Integer id) {
        return movieRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Movie with id " + id + " not found"
                        )
                );
    }

    public Movie createMovie(MovieRequest request) {

        Movie movie = new Movie();

        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setDuration(request.getDuration());
        movie.setAgeRating(request.getAgeRating());

        if (request.getActive() != null) {
            movie.setActive(request.getActive());
        } else {
            movie.setActive(true);
        }

        return movieRepository.save(movie);
    }

    public Movie updateMovie(Integer id, MovieRequest request) {

        Movie movie = getMovieById(id);

        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setDuration(request.getDuration());
        movie.setAgeRating(request.getAgeRating());

        if (request.getActive() != null) {
            movie.setActive(request.getActive());
        }

        return movieRepository.save(movie);
    }

    public void deleteMovie(Integer id) {

        Movie movie = getMovieById(id);

        movieRepository.delete(movie);
    }


}
