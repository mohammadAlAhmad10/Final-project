package com.dentist.app.repository;

import com.dentist.app.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    Optional<Doctor> findByUserName(String userName);

    List<Doctor> findByDoctorIdNotIn(Collection<Integer> doctorIds);
}
