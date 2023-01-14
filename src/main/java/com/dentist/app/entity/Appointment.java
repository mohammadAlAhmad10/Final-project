package com.dentist.app.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Setter
@Getter
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private Integer time;
    @ManyToOne
    @JoinColumn(name = "doctor")
    private Doctor doctor;
    @ManyToOne
    @JoinColumn(name = "patient")
    private Patient patient;
    @Column
    private Boolean status;
}
