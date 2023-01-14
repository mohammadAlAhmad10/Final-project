package com.dentist.app.service;

import com.dentist.app.entity.Appointment;
import com.dentist.app.entity.Doctor;
import com.dentist.app.entity.Patient;
import com.dentist.app.exception.AppointmentAlreadyExist;
import com.dentist.app.exception.InvalidAppointmentDate;
import com.dentist.app.exception.NoSuchAppointmentException;
import com.dentist.app.exception.NotAuthorizedException;
import com.dentist.app.exception.PatientNotFoundException;
import com.dentist.app.repository.AppointmentRepository;
import com.dentist.app.repository.DoctorRepository;
import com.dentist.app.repository.PatientRepository;
import com.dentist.app.service.request.DoctorCreateAppointmentRequest;
import com.dentist.app.service.request.DoctorUpdateProfileRequest;
import com.dentist.app.service.request.ReportRequest;
import com.dentist.app.service.response.AppointmentResponse;
import com.dentist.app.service.response.DoctorResponse;
import com.dentist.app.service.response.PatientResponse;
import com.dentist.app.shared.CSVHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;


    public void updateDoctorProfile(DoctorUpdateProfileRequest doctorUpdateProfileRequest, String token) {
        Doctor doctor = verifyToken(token);
        doctor.setDoctorName(doctorUpdateProfileRequest.getDoctorName());
        doctor.setPassword(doctorUpdateProfileRequest.getPassword());
        doctor.setSpeciality(doctorUpdateProfileRequest.getSpeciality());
        doctor.setPhoneNumber(doctorUpdateProfileRequest.getPhoneNumber());
        doctorRepository.save(doctor);
    }

    public List<String> getAvailableTime(String token) {
        Doctor doctor = verifyToken(token);
        LocalDate date = LocalDate.now();
        List<String> times = new ArrayList<>();
        List<Integer> appointments = appointmentRepository.findByDateAndDoctor(date, doctor).stream().map(appointment -> appointment.getTime()).collect(Collectors.toList());
        for (int x = 8; x < 17; x++) {
            if (!appointments.contains(x)) {
                times.add(x + ":" + "00 - " + (x + 1) + ":" + "00");
            }
        }
        return times;
    }

    public List<AppointmentResponse> getAppointmentTimeLine(String token) {
        Doctor doctor = verifyToken(token);
        LocalDate date = LocalDate.now();
        return appointmentRepository.findByDateAndDoctor(date, doctor).stream().map(
                appointment -> new AppointmentResponse(
                        appointment.getDate(),
                        appointment.getTime(),
                        new DoctorResponse(
                                appointment.getDoctor().getDoctorName(),
                                appointment.getDoctor().getPhoneNumber(),
                                appointment.getDoctor().getNationalId(),
                                appointment.getDoctor().getSpeciality()),
                        new PatientResponse(
                                appointment.getPatient().getPatientName(),
                                appointment.getPatient().getPhoneNumber(),
                                appointment.getPatient().getAge(),
                                appointment.getPatient().getGender()
                        ),
                        appointment.getStatus())
        ).collect(Collectors.toList());
    }

    public void createAppointment(DoctorCreateAppointmentRequest doctorCreateAppointmentRequest, String token) {
        Doctor doctor = verifyToken(token);
        Optional<Patient> optionalPatient = patientRepository.findById(doctorCreateAppointmentRequest.getPatientId());
        if (doctorCreateAppointmentRequest.getDate().compareTo(LocalDate.now()) <= 0) {
            throw new InvalidAppointmentDate("Appointment Date is Invalid!");
        }
        if (!optionalPatient.isPresent()) {
            throw new PatientNotFoundException("Patient Doesn't Exist");
        }
        if (!appointmentRepository.findByDoctorAndDateAndTimeAndPatient(doctor,
                doctorCreateAppointmentRequest.getDate(),
                doctorCreateAppointmentRequest.getTime(),
                optionalPatient.get()).isEmpty()) {
            throw new AppointmentAlreadyExist("Appointment Already Exist");
        }
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(optionalPatient.get());
        appointment.setTime(doctorCreateAppointmentRequest.getTime());
        appointment.setDate(doctorCreateAppointmentRequest.getDate());
        appointmentRepository.save(appointment);
    }

    public void cancelAppointment(Integer appointmentId, String token) {
        Doctor doctor = verifyToken(token);
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);

        if (!optionalAppointment.isPresent()) {
            throw new NoSuchAppointmentException("Appointment Doesn't Exist");
        }
        if (!optionalAppointment.get().getDoctor().getDoctorId().equals(doctor.getDoctorId())) {
            throw new NotAuthorizedException("Unauthorized");
        }
        appointmentRepository.deleteById(appointmentId);
    }

    public void updateAppointment(Integer appointmentId, String token) {
        verifyToken(token);
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        if (!optionalAppointment.isPresent()) {
            throw new NoSuchAppointmentException("No such appointment");
        }
        Appointment appointment = optionalAppointment.get();
        appointment.setStatus(true);
        appointmentRepository.save(appointment);
    }

    private Doctor verifyToken(String token) {
        String decodedToken = new String(Base64.getDecoder().decode(token));
        String userName = decodedToken.split(":")[0];
        Optional<Doctor> optionalDoctor = doctorRepository.findByUserName(userName);
        if (!optionalDoctor.isPresent()) {
            throw new NotAuthorizedException("Not Authorized");
        }
        return optionalDoctor.get();
    }

    public ByteArrayInputStream getDoctorReport(ReportRequest reportRequest, String token) {
        Doctor doctor = verifyToken(token);
        List<AppointmentResponse> appointments = appointmentRepository
                .findByDoctorAndDateBetween(doctor, reportRequest.getFrom(), reportRequest.getTo())
                .stream()
                .map(
                        appointment -> new AppointmentResponse(
                                appointment.getDate(),
                                appointment.getTime(),
                                new DoctorResponse(
                                        appointment.getDoctor().getDoctorName(),
                                        appointment.getDoctor().getPhoneNumber(),
                                        appointment.getDoctor().getNationalId(),
                                        appointment.getDoctor().getSpeciality()),
                                new PatientResponse(
                                        appointment.getPatient().getPatientName(),
                                        appointment.getPatient().getPhoneNumber(),
                                        appointment.getPatient().getAge(),
                                        appointment.getPatient().getGender()
                                ),
                                appointment.getStatus())

                ).collect(Collectors.toList());
        return CSVHelper.appointmentsToCSV(appointments);
    }

    public Integer getPatientVisitCount(Integer patientId, String token) {
        Doctor doctor = verifyToken(token);
        return appointmentRepository.countByDoctor_doctorIdAndPatient_patientId(doctor.getDoctorId(), patientId);
    }

    public Set<PatientResponse> getPatients(String token) {
        Doctor doctor = verifyToken(token);
        return appointmentRepository.findByDoctor_DoctorId(doctor.getDoctorId()).stream().map(
                appointment -> new PatientResponse(
                        appointment.getPatient().getPatientName(),
                        appointment.getPatient().getPhoneNumber(),
                        appointment.getPatient().getAge(),
                        appointment.getPatient().getGender()
                )).collect(Collectors.toSet());
    }

    public PatientResponse getPatientInfo(Integer patientId, String token) {
        verifyToken(token);
        Optional<Patient> optionalPatient = patientRepository.findById(patientId);
        if (!optionalPatient.isPresent()) {
            throw new PatientNotFoundException("Patient Doesn't exist");
        }
        Patient patient = optionalPatient.get();
        return new PatientResponse(
                patient.getPatientName(),
                patient.getPhoneNumber(),
                patient.getAge(),
                patient.getGender()
        );
    }
}
