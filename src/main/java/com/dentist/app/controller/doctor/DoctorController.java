package com.dentist.app.controller.doctor;

import com.dentist.app.service.DoctorService;
import com.dentist.app.service.request.DoctorCreateAppointmentRequest;
import com.dentist.app.service.request.DoctorUpdateProfileRequest;
import com.dentist.app.service.request.ReportRequest;
import com.dentist.app.service.response.AppointmentResponse;
import com.dentist.app.service.response.PatientResponse;
import com.dentist.app.shared.SharedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("app/dentist/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PutMapping("update")
    public ResponseEntity<SharedResponse<String>> updateDoctor(
            @RequestBody DoctorUpdateProfileRequest doctorUpdateProfileRequest,
            @RequestHeader String token
    ) {
        doctorService.updateDoctorProfile(doctorUpdateProfileRequest, token);
        return ResponseEntity.ok(
                new SharedResponse<>(0, "Successful", "Doctor Updated Successfully")
        );
    }

    @GetMapping("available-time")
    public ResponseEntity<SharedResponse<List<String>>> updateDoctor(
            @RequestHeader String token
    ) {
        return ResponseEntity.ok(
                new SharedResponse<>(0, "Successful", doctorService.getAvailableTime(token))
        );
    }

    @GetMapping("appointment-timeline")
    public ResponseEntity<SharedResponse<List<AppointmentResponse>>> getAppointmentTimeline(@RequestHeader String token) {
        return ResponseEntity.ok(
                new SharedResponse<>(0, "Successful", doctorService.getAppointmentTimeLine(token))
        );
    }

    @PostMapping("create-appointment")
    public ResponseEntity<SharedResponse<String>> createAppointment(@RequestBody DoctorCreateAppointmentRequest doctorCreateAppointmentRequest,
                                                                    @RequestHeader String token) {
        doctorService.createAppointment(doctorCreateAppointmentRequest, token);
        return ResponseEntity.ok(
                new SharedResponse<>(0, "Successful", "Appointment created successfully")
        );
    }

    @DeleteMapping("cancel-appointment/{appointmentId}")
    public ResponseEntity<SharedResponse<String>> cancelAppointment(@PathVariable Integer appointmentId,
                                                                    @RequestHeader String token) {
        doctorService.cancelAppointment(appointmentId, token);
        return ResponseEntity.ok(
                new SharedResponse<>(0, "Successful", "Appointment cancelled successfully")
        );
    }

    @PutMapping("update-appointment/{appointmentId}")
    public ResponseEntity<SharedResponse<String>> updateAppointment(@PathVariable Integer appointmentId, @RequestHeader String token) {
        doctorService.updateAppointment(appointmentId, token);
        return ResponseEntity.ok(
                new SharedResponse<>(0, "Successful", "Appointment updated successfully")
        );
    }


    @GetMapping("patient-visit-count/{patientId}")
    public ResponseEntity<SharedResponse<String>> getPatientVisitCount(
            @PathVariable Integer patientId,
            @RequestHeader String token
    ) {
        return ResponseEntity.ok(
                new SharedResponse<>(0, "Successful", doctorService.getPatientVisitCount(patientId, token) + " visits")
        );
    }

    @GetMapping("patients")
    public ResponseEntity<SharedResponse<Set<PatientResponse>>> getPatients(
            @RequestHeader String token
    ) {
        return ResponseEntity.ok(
                new SharedResponse<>(0, "Successful", doctorService.getPatients(token))
        );
    }

    @GetMapping("patient/{patientId}")
    public ResponseEntity<SharedResponse<PatientResponse>> getPatientInfo(
            @RequestHeader String token,
            @PathVariable Integer patientId
    ) {
        return ResponseEntity.ok(
                new SharedResponse<>(0, "Successful", doctorService.getPatientInfo(patientId, token))
        );
    }

    @GetMapping("report")
    public ResponseEntity<Resource> getDoctorReport(
            @RequestBody ReportRequest reportRequest,
            @RequestHeader String token
    ) {
        String filename = "report.csv";
        InputStreamResource file = new InputStreamResource(doctorService.getDoctorReport(reportRequest, token));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }

}
