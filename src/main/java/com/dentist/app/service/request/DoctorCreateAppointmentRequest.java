package com.dentist.app.service.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@Data
public class DoctorCreateAppointmentRequest {
    private Integer patientId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @Min(8)
    @Max(15)
    private Integer time;
}
