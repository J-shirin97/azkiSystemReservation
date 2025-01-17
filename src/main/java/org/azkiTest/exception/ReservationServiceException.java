package org.azkiTest.exception;

public class ReservationServiceException extends Exception {
    public ReservationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}