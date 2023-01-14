package com.dentist.app.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DoctorAlreadyExist extends RuntimeException {
    public DoctorAlreadyExist(String message) {
        super(message);
    }
}
