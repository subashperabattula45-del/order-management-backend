package com.subash.ordermanagement.exception;

public class InvalidPaymentStatusException extends RuntimeException {

    public InvalidPaymentStatusException(String message) {
        super(message);
    }
}