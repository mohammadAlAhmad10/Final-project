package com.dentist.app.service;

import com.dentist.app.entity.Appointment;
import com.dentist.app.entity.Doctor;
import com.dentist.app.entity.Patient;
import com.dentist.app.exception.AppointmentAlreadyExist;
import com.dentist.app.exception.DoctorNotFoundException;
import com.dentist.app.exception.InvalidAppointmentDate;
import com.dentist.app.exception.NoSuchAppointmentException;
import com.dentist.app.exception.NotAuthorizedException;
import com.dentist.app.repository.AppointmentRepository;
import com.dentist.app.repository.DoctorRepository;
import com.dentist.app.repository.PatientRepository;
import com.dentist.app.service.request.ListAvailableDoctorRequest;
import com.dentist.app.service.request.PatientCreateAppointmentRequest;
import com.dentist.app.service.request.PatientUpdateProfileRequest;
import com.dentist.app.service.request.ReportRequest;
import com.dentist.app.service.response.AppointmentResponse;
import com.dentist.app.service.response.DoctorResponse;
import com.dentist.app.service.response.PatientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;

    public void updatePatientProfile(PatientUpdateProfileRequest patientUpdateProfileRequest, String token) {
        Patient patient = verifyToken(token);
        patient.setPatientName(patientUpdateProfileRequest.getPatientName());
        patient.setUserName(patientUpdateProfileRequest.getUserName());
        patient.setPassword(patientUpdateProfileRequest.getPassword());
        patient.setAge(patientUpdateProfileRequest.getAge());
        patient.setGender(patientUpdateProfileRequest.getGender());
        patient.setPhoneNumber(patientUpdateProfileRequest.getPhoneNumber());
        patientRepository.save(patient);
    }

    public List<DoctorResponse> listDoctor(String token) {
        verifyToken(token);
        return doctorRepository.findAll().stream().map(
                doctor -> new DoctorResponse(doctor.getDoctorName(), doctor.getPhoneNumber(), doctor.getPhoneNumber(), doctor.getSpeciality())
        ).collect(Collectors.toList());
    }

    public List<DoctorResponse> listAvailableDoctorByDateTime(ListAvailableDoctorRequest listAvailableDoctorRequest, String token) {
        verifyToken(token);
        List<Integer> doctorIds = appointmentRepository.findByDateAndTime(listAvailableDoctorRequest.getDate(), listAvailableDoctorRequest.getTime()).stream().map(
                appointment -> appointment.getDoctor().getDoctorId()
        ).collect(Collectors.toList());
        if (doctorIds.isEmpty()) {
            return doctorRepository.findAll().stream().map(
                    doctor -> new DoctorResponse(
                            doctor.getDoctorName(),
                            doctor.getPhoneNumber(),
                            doctor.getNationalId(),
                            doctor.getSpeciality())
            ).collect(Collectors.toList());
        }
        return doctorRepository.findByDoctorIdNotIn(doctorIds).stream().map(
                doctor -> new DoctorResponse(
                        doctor.getDoctorName(),
                        doctor.getPhoneNumber(),
                        doctor.getNationalId(),
                        doctor.getSpeciality())
        ).collect(Collectors.toList());
    }

    public void createAppointment(PatientCreateAppointmentRequest patientCreateAppointmentRequest, String token) {
        Patient patient = verifyToken(token);
        Optional<Doctor> optionalDoctor = doctorRepository.findById(patientCreateAppointmentRequest.getDoctorId());
        if (patientCreateAppointmentRequest.getDate().compareTo(LocalDate.now()) <= 0) {
            throw new InvalidAppointmentDate("Appointment Date is Invalid!");
        }
        if (!optionalDoctor.isPresent()) {
            throw new DoctorNotFoundException("Doctor Doesn't Exist");
        }
        if (!appointmentRepository.findByDoctorAndDateAndTimeAndPatient(optionalDoctor.get(),
                patientCreateAppointmentRequest.getDate(),
                patientCreateAppointmentRequest.getTime(),
                patient).isEmpty()) {
            throw new AppointmentAlreadyExist("Appointment Already Exist");
        }
        Appointment appointment = new Appointment();
        appointment.setDoctor(optionalDoctor.get());
        appointment.setPatient(patient);
        appointment.setTime(patientCreateAppointmentRequest.getTime());
        appointment.setDate(patientCreateAppointmentRequest.getDate());
        appointmentRepository.save(appointment);

    }

    public void cancelAppointment(Integer appointmentId, String token) {
        Patient patient = verifyToken(token);
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);

        if (!optionalAppointment.isPresent()) {
            throw new NoSuchAppointmentException("Appointment Doesn't Exist");
        }
        if (!optionalAppointment.get().getPatient().getPatientId().equals(patient.getPatientId())) {
            throw new NotAuthorizedException("Unauthorized");
        }
        appointmentRepository.deleteById(appointmentId);
    }

    public List<AppointmentResponse> showDentistReport(ReportRequest reportRequest, String token) {
        Patient patient = verifyToken(token);
        return appointmentRepository
                .findByPatientAndDateBetween(patient, reportRequest.getFrom(), reportRequest.getTo())
                .stream()
                .map(appointment -> new AppointmentResponse(
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


    private Patient verifyToken(String token) {
        String decodedToken = new String(Base64.getDecoder().decode(token));
        String userName = decodedToken.split(":")[0];
        Optional<Patient> optionalPatient = patientRepository.findByUserName(userName);
        if (!optionalPatient.isPresent()) {
            throw new NotAuthorizedException("Not Authorized");
        }
        return optionalPatient.get();
    }

}
