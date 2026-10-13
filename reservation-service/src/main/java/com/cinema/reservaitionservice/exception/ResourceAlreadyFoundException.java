package com.cinema.reservaitionservice.exception;

public class ResourceAlreadyFoundException extends RuntimeException {
    public ResourceAlreadyFoundException(String message) {
        super(message);
    }
}
