package com.dentist.app.service.request;

import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class DoctorUpdateProfileRequest {
    @Size(min = 5, max = 64, message = "Invalid name length")
    private String doctorName;
    @Size(min = 8, max = 36, message = "Password must be 8-36 characters")
    private String password;
    @NotBlank
    private String speciality;
    @Size(min = 10, max = 10, message = "National id must be 10 characters long")
    private String nationalId;
    @Digits(message = "Invalid mobile number", integer = 10, fraction = 0)
    private String phoneNumber;
}
