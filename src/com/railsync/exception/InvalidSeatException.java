package com.railsync.exception;

// Thrown when a requested seat number doesn't exist on this train at all
public class InvalidSeatException extends Exception {
    public InvalidSeatException(String message) {
        super(message);
    }
}