package com.dentist.app.repository;

import com.dentist.app.entity.Appointment;
import com.dentist.app.entity.Doctor;
import com.dentist.app.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    Integer countByDoctor_doctorIdAndPatient_patientId(Integer doctorId, Integer patientId);

    List<Appointment> findByDoctor_DoctorId(Integer doctorId);

    List<Appointment> findByDateAndDoctor(LocalDate date, Doctor doctor);

    void deleteByDateAndDoctorAndTime(LocalDate date, Doctor doctor, Integer hour);

    void deleteByDateAndPatientAndTime(LocalDate date, Patient patient, Integer hour);

    List<Appointment> findByPatientAndDateBetween(Patient patient, LocalDate from, LocalDate to);

    List<Appointment> findByDoctorAndDateBetween(Doctor doctor, LocalDate from, LocalDate to);

    List<Appointment> findByDoctorAndDateAndTimeAndPatient(Doctor doctor, LocalDate date, Integer time, Patient patient);

    List<Appointment> findByDateAndTime(LocalDate date, Integer time);
}
