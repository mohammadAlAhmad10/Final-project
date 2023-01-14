package com.dentist.app.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer patientId;
    @Column(nullable = false, unique = true)
    private String userName;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String patientName;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private Integer age;
    @Column(nullable = false)
    private Boolean isLoggedIn;
    @Column(nullable = false)
    private Character gender;
    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;

}
