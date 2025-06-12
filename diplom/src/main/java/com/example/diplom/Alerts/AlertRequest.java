package com.example.diplom.Alerts;

import com.example.diplom.model.AlertType;
import lombok.Data;

@Data
public class AlertRequest {

    private String plateNumber;
    private AlertType alertType;
    private String description;
    private String priority;
    private String actions;
    private boolean notifyPatrol;
}
