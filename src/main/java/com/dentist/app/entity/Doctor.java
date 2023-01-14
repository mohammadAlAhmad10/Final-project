package com.dentist.app.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer doctorId;
    @Column(nullable = false, unique = true)
    private String userName;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String doctorName;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String nationalId;
    @Column(nullable = false)
    private String speciality;
    @Column(nullable = false)
    private Boolean isLoggedIn;
    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;
}
