package com.dentist.app.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidUserOrPasswordException extends RuntimeException {
    public InvalidUserOrPasswordException(String message) {
        super(message);
    }
}
