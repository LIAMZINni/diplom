package com.example.diplom.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Data
@Entity
public class WantedVehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plate_number", nullable = false)
    private String plateNumber;

    @Column(name = "vin_code")
    private String vinCode;

    @Column(nullable = false)
    private String reason; // "Угон", "Кража", "Розыск по делу" и т.д.

    @Column(name = "report_date")
    private LocalDate reportDate;

    @Column(name = "reporting_authority")
    private String reportingAuthority; // Кто объявил в розыск

    @Column(name = "is_active")
    private boolean isActive = true;

    private String declaredBy;

    @Column(name = "contact_info")
    private String contactInfo; // Контакты для связи

    @Column(name = "additional_details")
    private String additionalDetails; // Особые приметы и доп. информация
}
