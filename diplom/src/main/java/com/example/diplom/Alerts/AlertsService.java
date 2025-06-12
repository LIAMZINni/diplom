package com.example.diplom.Alerts;

import com.example.diplom.model.AlertType;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class AlertsService {
    @Autowired
    private AlertRepository alertRepository;




    public VehicleAlert createAlert(String plateNumber,
                                    AlertType alertType,
                                    String description,
                                    String priority,
                                    Long inspectorId) {

        VehicleAlert alert = new VehicleAlert();
        alert.setPlateNumber(plateNumber);
        alert.setAlertType(alertType);
        alert.setDescription(description);
        alert.setCreatedBy(inspectorId);
        alert.setCreatedAt(LocalDateTime.now());

        VehicleAlert savedAlert = alertRepository.save(alert);



        return savedAlert;
    }
}
