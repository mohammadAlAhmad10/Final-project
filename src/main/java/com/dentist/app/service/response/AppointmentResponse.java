package com.dentist.app.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class AppointmentResponse {
    private LocalDate date;
    private Integer time;
    private DoctorResponse doctor;
    private PatientResponse patient;
    private Boolean status;
}
