package com.dentist.app.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(String message) {
        super(message);
    }
}
