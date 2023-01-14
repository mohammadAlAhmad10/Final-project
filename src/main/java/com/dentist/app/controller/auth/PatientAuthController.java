package com.dentist.app.controller.auth;

import com.dentist.app.service.PatientAuthService;
import com.dentist.app.service.request.LoginRequest;
import com.dentist.app.service.request.PatientRequest;
import com.dentist.app.shared.SharedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("app/dentist/patient")
public class PatientAuthController {

    @Autowired
    private PatientAuthService patientAuthService;

    @PostMapping("register")
    public ResponseEntity<SharedResponse<String>> registerPatient(@Valid @RequestBody PatientRequest patientRequest) {
        patientAuthService.registerPatient(patientRequest);
        return ResponseEntity.ok(
                new SharedResponse<>(0, "Successful", "Patient Registered Successfully")
        );
    }

    @PostMapping("login")
    public ResponseEntity<SharedResponse<String>> loginPatient(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(
                new SharedResponse<>(0, "Successful", patientAuthService.loginPatient(loginRequest))
        );
    }

}
