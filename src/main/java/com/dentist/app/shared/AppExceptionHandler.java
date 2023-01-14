package com.dentist.app.shared;

import com.dentist.app.exception.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<SharedResponse<Map<String, List<String>>>> constraintViolationException(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, List<String>> body = new HashMap<>();
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        body.put("errors", errors);
        return new ResponseEntity<>(new SharedResponse<>(1, "Failed", body), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<SharedResponse<String>> constraintViolationException(InvalidUserOrPasswordException ex, WebRequest request) {
        return new ResponseEntity<>(new SharedResponse<>(1, "Failed", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<SharedResponse<String>> doctorAlreadyExistException(DoctorAlreadyExist ex, WebRequest request) {
        return new ResponseEntity<>(new SharedResponse<>(1, "Failed", ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<SharedResponse<String>> patientAlreadyExistException(PatientAlreadyExist ex, WebRequest request) {
        return new ResponseEntity<>(new SharedResponse<>(1, "Failed", ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<SharedResponse<String>> missingRequestHeaderException(MissingRequestHeaderException ex, WebRequest request) {
        return new ResponseEntity<>(new SharedResponse<>(1, "Failed", "not authorized"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<SharedResponse<String>> notAuthorizedException(NotAuthorizedException ex, WebRequest request) {
        return new ResponseEntity<>(new SharedResponse<>(1, "Failed", ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<SharedResponse<String>> patientNotFoundException(PatientNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(new SharedResponse<>(1, "Failed", ex.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<SharedResponse<String>> doctorNotFoundException(DoctorNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(new SharedResponse<>(1, "Failed", ex.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<SharedResponse<String>> noSuchAppointmentExceptionException(NoSuchAppointmentException ex, WebRequest request) {
        return new ResponseEntity<>(new SharedResponse<>(1, "Failed", ex.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<SharedResponse<String>> appointmentAlreadyExistException(AppointmentAlreadyExist ex, WebRequest request) {
        return new ResponseEntity<>(new SharedResponse<>(1, "Failed", ex.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }
    @ExceptionHandler
    public ResponseEntity<SharedResponse<String>> appointmentAlreadyExistException(InvalidAppointmentDate ex, WebRequest request) {
        return new ResponseEntity<>(new SharedResponse<>(1, "Failed", ex.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }
}
