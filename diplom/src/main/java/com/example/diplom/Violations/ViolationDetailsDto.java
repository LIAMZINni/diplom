package com.example.diplom.Violations;

import com.example.diplom.model.CameraReport;
import com.example.diplom.model.Driver;
import com.example.diplom.model.Inspector;
import com.example.diplom.model.Vehicle;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ViolationDetailsDto {
    private Long id;
    private LocalDateTime violationTime;
    private int fineAmount;
    private String uin;
    private Driver driver;
    private Vehicle vehicle;
    private CameraReport cameraReport;
    private Inspector inspector;
}
