package com.dentist.app.service;

import com.dentist.app.entity.Doctor;
import com.dentist.app.exception.DoctorAlreadyExist;
import com.dentist.app.exception.InvalidUserOrPasswordException;
import com.dentist.app.repository.DoctorRepository;
import com.dentist.app.service.request.DoctorRequest;
import com.dentist.app.service.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

@Service
public class DoctorAuthService {
    @Autowired
    private DoctorRepository doctorRepository;

    public void registerDoctor(DoctorRequest request) {
        if (doctorRepository.findByUserName(request.getUserName()).isPresent()) {
            throw new DoctorAlreadyExist("Doctor already exist");
        }
        Doctor doctor = new Doctor();
        doctor.setPassword(request.getPassword());
        doctor.setDoctorName(request.getDoctorName());
        doctor.setSpeciality(request.getSpeciality());
        doctor.setNationalId(request.getNationalId());
        doctor.setPhoneNumber(request.getPhoneNumber());
        doctor.setUserName(request.getUserName());
        doctor.setIsLoggedIn(false);
        doctorRepository.save(doctor);
    }

    public String loginDoctor(LoginRequest loginRequest) {
        Optional<Doctor> optionalDoctor = doctorRepository.findByUserName(loginRequest.getUserName());
        if (optionalDoctor.isPresent()) {
            optionalDoctor.get().setIsLoggedIn(true);
            if (Objects.equals(optionalDoctor.get().getPassword(), loginRequest.getPassword())) {
                return Base64.getEncoder().encodeToString((loginRequest.getUserName() + ":" + loginRequest.getPassword()).getBytes());
            }
        }
        throw new InvalidUserOrPasswordException("Invalid Username or password!");
    }
}
