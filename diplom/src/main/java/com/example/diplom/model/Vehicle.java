package com.example.diplom.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
@Data
@Getter
@Setter
@Entity
public class Vehicle {
    @Id
    private String plateNumber; // Госномер

    private String model;
    private String make;
    private String CTC;
    private String color;
    private Integer year;
    private String vin;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;


    @Enumerated(EnumType.STRING)
    private VehicleType type; // Легковое, грузовое и т.д.

    @Column(name = "insurance_number",unique = true)
    private String insuranceNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_license_number")
    private Driver driver;


}
