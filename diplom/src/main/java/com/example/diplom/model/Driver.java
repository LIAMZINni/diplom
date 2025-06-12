package com.example.diplom.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Driver {
    @Id
    private String licenseNumber; // Номер ВУ

    private String fullName;
    private LocalDate birthDate;
    private String address;
    @OneToMany
    private List<Vehicle> vehicleList;
}
