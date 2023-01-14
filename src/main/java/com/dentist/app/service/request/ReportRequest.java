package com.dentist.app.service.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class ReportRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;
}
