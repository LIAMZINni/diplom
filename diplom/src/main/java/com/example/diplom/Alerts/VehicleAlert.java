package com.example.diplom.Alerts;

import com.example.diplom.model.AlertType;
import com.example.diplom.model.Inspector;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
public class VehicleAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String plateNumber;


    @Enumerated(EnumType.STRING)
    private AlertType alertType; // "STOLEN", "WRONG_PLATE", "DEBTOR"


    private String description;

    @Column(name = "created_by")
    private Long createdBy; // ID инспектора

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_resolved")
    private boolean isResolved = false;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "resolved_by")
    private Long resolvedBy;

    @Column(columnDefinition = "TEXT")
    private String actions;


}




