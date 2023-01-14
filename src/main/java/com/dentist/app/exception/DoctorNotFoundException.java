package com.dentist.app.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DoctorNotFoundException extends RuntimeException {
    public DoctorNotFoundException(String message) {
        super(message);
    }
}
