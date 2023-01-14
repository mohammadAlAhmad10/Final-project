package com.dentist.app.exception;

public class InvalidAppointmentDate extends RuntimeException {
    public InvalidAppointmentDate(String message) {
        super(message);
    }}