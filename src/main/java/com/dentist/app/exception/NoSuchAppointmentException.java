package com.dentist.app.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoSuchAppointmentException extends RuntimeException {
    public NoSuchAppointmentException(String message) {
        super(message);
    }
}
