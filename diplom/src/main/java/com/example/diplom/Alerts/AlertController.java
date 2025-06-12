package com.example.diplom.Alerts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/alerts")
public class AlertController {

    private final AlertsService alertService;

    @Autowired
    public AlertController(AlertsService alertService) {
        this.alertService = alertService;
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> createAlert(
            @RequestBody AlertRequest request
            ) {

        try {
            Long inspectorId = Long.valueOf(1);
            VehicleAlert alert = alertService.createAlert(
                    request.getPlateNumber(),
                    request.getAlertType(),
                    request.getDescription(),
                    request.getPriority(),
                    inspectorId
            );

            return ResponseEntity.ok(alert);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при создании ориентировки");
        }
    }
}
