package org.azkiTest.exception;

public class ReservationAlreadyExistsException extends Exception {
    public ReservationAlreadyExistsException(String message) {
        super(message);
    }
}
