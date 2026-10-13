package com.cinema.movieservice.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String message) {

        super(message);
    }
}
