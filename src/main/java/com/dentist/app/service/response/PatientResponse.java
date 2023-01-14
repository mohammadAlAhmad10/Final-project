package com.dentist.app.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PatientResponse {

    private String patientName;
    private String phoneNumber;
    private Integer age;
    private Character gender;
}
