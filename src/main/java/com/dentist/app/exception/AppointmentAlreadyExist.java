package com.dentist.app.exception;

public class AppointmentAlreadyExist extends RuntimeException {
    public AppointmentAlreadyExist(String message) {
        super(message);
    }
}
