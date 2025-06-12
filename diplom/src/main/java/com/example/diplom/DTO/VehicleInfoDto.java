package com.example.diplom.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VehicleInfoDto {
    private String plateNumber;
    private String makeModel;
    private String color;
    private String vin;
    private String ownerName;
    private boolean wanted;
    private String wantedReason;
    private String wantedBy;
    private LocalDate wantedSince;
    private String wantedDetails;
    private String contactInfo;
//    private List<ViolationHistoryItem> violationHistory;
}