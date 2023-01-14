package com.dentist.app.service.request;

import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
public class PatientUpdateProfileRequest {
    @Size(min = 5, max = 15, message = "Username must be 5-15 characters")
    private String userName;
    @Size(min = 8, max = 36, message = "Password must be 8-36 characters")
    private String password;
    @Size(min = 5, max = 64, message = "Invalid name length")
    private String patientName;
    private Integer age;
    @Max(value = 120, message = "Invalid age")
    @Min(value = 0, message = "Invalid age")
    private Character gender;
    @Digits(message = "Invalid mobile number", integer = 10, fraction = 0)
    private String phoneNumber;

}
