package com.example.diplom.Violations;

import com.example.diplom.model.*;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
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
    private ViolationStatus status= ViolationStatus.PENDING;
    @Lob
    private byte[] prescriptionPdf;

    private double fineAmount;
    @PostLoad
    @PrePersist
    @PreUpdate
    public void calculateFine() {
        if (this.type != null && this.cameraReport != null
                && this.cameraReport.getDetectedSpeed() != null
                && this.cameraReport.getSpeedLimit() != null) {

            this.fineAmount = this.type.calculateFine(this);
        }
    }
}
