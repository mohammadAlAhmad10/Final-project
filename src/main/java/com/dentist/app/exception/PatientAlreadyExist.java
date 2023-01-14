package com.dentist.app.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PatientAlreadyExist extends RuntimeException {
    public PatientAlreadyExist(String message) {
        super(message);
    }
}
