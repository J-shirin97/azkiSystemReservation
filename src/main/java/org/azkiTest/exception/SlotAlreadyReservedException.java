package org.azkiTest.exception;

public class SlotAlreadyReservedException extends Exception {
    public SlotAlreadyReservedException(String message) {
        super(message);
    }
}