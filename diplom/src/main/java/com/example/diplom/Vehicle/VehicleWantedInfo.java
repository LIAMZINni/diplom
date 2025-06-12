package com.example.diplom.Vehicle;

import com.example.diplom.model.Vehicle;
import com.example.diplom.model.WantedVehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class VehicleWantedInfo {
    private Vehicle vehicle;       // Может быть null
    private WantedVehicle wantedInfo;
    public boolean isWanted() {
        return wantedInfo != null && wantedInfo.isActive();
    }

    // Геттеры
    public Vehicle getVehicle() {
        return vehicle != null ? vehicle : new Vehicle(); // Пустой объект для безопасности
    }
}
