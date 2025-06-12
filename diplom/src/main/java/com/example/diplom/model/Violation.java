package com.example.diplom.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Violation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;


    @ManyToOne
    @JoinColumn(name = "inspector_id")
    private Inspector inspector;



    private LocalDateTime violationTime;


    @Enumerated(EnumType.STRING)
    private ViolationType type;



    @OneToOne
    @JoinColumn(name = "cameraReportId")
    private CameraReport cameraReport;

    private String comment;

    @Enumerated(EnumType.STRING)
    private ViolationStatus status= ViolationStatus.PENDING;;
}
