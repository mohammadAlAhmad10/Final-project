package com.dentist.app.service;

import com.dentist.app.entity.Patient;
import com.dentist.app.exception.InvalidUserOrPasswordException;
import com.dentist.app.exception.PatientAlreadyExist;
import com.dentist.app.repository.PatientRepository;
import com.dentist.app.service.request.LoginRequest;
import com.dentist.app.service.request.PatientRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

@Service
public class PatientAuthService {
    @Autowired
    private PatientRepository patientRepository;

    public void registerPatient(PatientRequest request) {
        if (patientRepository.findByUserName(request.getUserName()).isPresent()) {
            throw new PatientAlreadyExist("Patient Already Exist");
        }
        Patient patient = new Patient();
        patient.setAge(request.getAge());
        patient.setPassword(request.getPassword());
        patient.setPatientName(request.getPatientName());
        patient.setPhoneNumber(request.getPhoneNumber());
        patient.setGender(request.getGender());
        patient.setUserName(request.getUserName());
        patient.setIsLoggedIn(false);
        patientRepository.save(patient);
    }

    public String loginPatient(LoginRequest loginRequest) {
        Optional<Patient> optionalPatient = patientRepository.findByUserName(loginRequest.getUserName());
        if (optionalPatient.isPresent()) {
            optionalPatient.get().setIsLoggedIn(true);
            if (Objects.equals(optionalPatient.get().getPassword(), loginRequest.getPassword())) {
                return Base64.getEncoder().encodeToString((loginRequest.getUserName() + ":" + loginRequest.getPassword()).getBytes());
            }
        }
        throw new InvalidUserOrPasswordException("Invalid Username or password!");
    }
}
