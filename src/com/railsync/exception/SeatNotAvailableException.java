package com.railsync.exception;

// Thrown when a customer tries to book a seat that's already taken
public class SeatNotAvailableException extends Exception {
    public SeatNotAvailableException(String message) {
        super(message);
    }
}