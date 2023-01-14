package com.dentist.app.controller.auth;

import com.dentist.app.service.DoctorAuthService;
import com.dentist.app.service.request.DoctorRequest;
import com.dentist.app.service.request.LoginRequest;
import com.dentist.app.shared.SharedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("app/dentist/doctor")
public class DoctorAuthController {

    @Autowired
    private DoctorAuthService doctorAuthService;

    @PostMapping("register")
    public ResponseEntity<SharedResponse<String>> registerDoctor(@Valid @RequestBody DoctorRequest doctorRequest) {
        doctorAuthService.registerDoctor(doctorRequest);
        return ResponseEntity.ok(
                new SharedResponse<>(0, "Successful", "Doctor Registered Successfully")
        );
    }

    @PostMapping("login")
    public ResponseEntity<SharedResponse<String>> loginDoctor(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(
                new SharedResponse<>(0, "Successful", doctorAuthService.loginDoctor(loginRequest))
        );
    }

}
