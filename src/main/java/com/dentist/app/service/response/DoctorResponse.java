package com.dentist.app.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
public class DoctorResponse {
    @Size(min = 5, max = 64, message = "Invalid name length")
    private String doctorName;
    @Digits(message = "Invalid mobile number", integer = 10, fraction = 0)
    private String phoneNumber;
    @Size(min = 10, max = 10, message = "National id must be 10 characters long")
    private String nationalId;
    private String speciality;
}
