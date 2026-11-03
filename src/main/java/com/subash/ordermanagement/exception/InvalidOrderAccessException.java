package com.subash.ordermanagement.exception;

public class InvalidOrderAccessException extends RuntimeException {

    public InvalidOrderAccessException(String message) {
        super(message);
    }
}