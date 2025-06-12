package com.example.diplom.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;


public class Citizen extends User{
    @Column(unique = true)
    private String driverLicenseNumber; // Номер ВУ

//    @OneToMany(mappedBy = "citizen")
//    private List<Appeal> appeals;
}
