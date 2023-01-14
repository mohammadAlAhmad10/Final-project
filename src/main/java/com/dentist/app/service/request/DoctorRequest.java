package com.dentist.app.service.request;

import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class DoctorRequest {
    @Size(min = 5, max = 15, message = "Username must be 5-15 characters")
    private String userName;
    @Size(min = 8, max = 36, message = "Password must be 8-36 characters")
    private String password;
    @Size(min = 5, max = 64, message = "Invalid name length")
    private String doctorName;
    @Digits(message = "Invalid mobile number", integer = 10, fraction = 0)
    private String phoneNumber;
    private Boolean isLoggedIn;
    @Size(min = 10, max = 10, message = "National id must be 10 characters long")
    private String nationalId;
    @NotBlank
    private String speciality;
}
