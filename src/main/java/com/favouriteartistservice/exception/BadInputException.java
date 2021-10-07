package com.favouriteartistservice.exception;

public class BadInputException extends RuntimeException {
    public BadInputException(String message) {
        super(message);
    }
}
