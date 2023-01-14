package com.dentist.app.controller.patient;

import com.dentist.app.service.PatientService;
import com.dentist.app.service.request.ListAvailableDoctorRequest;
import com.dentist.app.service.request.PatientCreateAppointmentRequest;
import com.dentist.app.service.request.PatientUpdateProfileRequest;
import com.dentist.app.service.request.ReportRequest;
import com.dentist.app.service.response.AppointmentResponse;
import com.dentist.app.service.response.DoctorResponse;
import com.dentist.app.shared.SharedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("app/dentist/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PutMapping("update")
    public ResponseEntity<SharedResponse<String>> updatePatientProfile(
            @RequestBody PatientUpdateProfileRequest patientUpdateProfileRequest,
            @RequestHeader String token
    ) {
        patientService.updatePatientProfile(patientUpdateProfileRequest, token);
        return ResponseEntity.ok(
                new SharedResponse<>(0, "Successful", "Update Successfully")
        );
    }

    @GetMapping("doctors")
    public ResponseEntity<SharedResponse<List<DoctorResponse>>> getDoctors(@RequestHeader String token) {
        return ResponseEntity.ok(
                new SharedResponse<>(0, "Successful", patientService.listDoctor(token))
        );
    }

    @GetMapping("available-doctors")
    public ResponseEntity<SharedResponse<List<DoctorResponse>>> getAvailableDoctors(
            @RequestBody ListAvailableDoctorRequest listAvailableDoctorRequest,
            @RequestHeader String token) {
        return ResponseEntity.ok(
                new SharedResponse<>(0, "Successful", patientService.listAvailableDoctorByDateTime(listAvailableDoctorRequest, token))
        );
    }

    @PostMapping("create-appointment")
    public ResponseEntity<SharedResponse<String>> createAppointment(
            @RequestBody PatientCreateAppointmentRequest patientCreateAppointmentRequest,
            @RequestHeader String token
    ) {
        patientService.createAppointment(patientCreateAppointmentRequest, token);
        return ResponseEntity.ok(
                new SharedResponse<>(0, "Successful", "Appointment created Successfully")
        );
    }

    @DeleteMapping("cancel-appointment/{appointmentId}")
    public ResponseEntity<SharedResponse<String>> cancelAppointment(
            @PathVariable Integer appointmentId,
            @RequestHeader String token
    ) {
        patientService.cancelAppointment(appointmentId, token);
        return ResponseEntity.ok(
                new SharedResponse<>(0, "Successful", "Appointment Cancelled")
        );
    }

    @GetMapping("report")
    public ResponseEntity<SharedResponse<List<AppointmentResponse>>> showDentistReport(
            @RequestBody ReportRequest reportRequest,
            @RequestHeader String token
    ) {
        return ResponseEntity.ok(
                new SharedResponse<>(0, "Successful", patientService.showDentistReport(reportRequest, token))
        );
    }


}
