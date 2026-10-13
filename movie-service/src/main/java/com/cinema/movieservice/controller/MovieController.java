package com.cinema.movieservice.controller;

import com.cinema.movieservice.dto.MovieRequest;
import com.cinema.movieservice.model.Movie;
import com.cinema.movieservice.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable Integer id) {
        return movieService.getMovieById(id);
    }

    @PostMapping
    public ResponseEntity<Movie> createMovie(
            @Valid @RequestBody MovieRequest request) {

        Movie movie = movieService.createMovie(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(movie);
    }

    @PutMapping("/{id}")
    public Movie updateMovie(
            @PathVariable Integer id,
            @Valid @RequestBody MovieRequest request) {

        return movieService.updateMovie(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(
            @PathVariable Integer id) {

        movieService.deleteMovie(id);

        return ResponseEntity.noContent().build();
    }

}
