package com.example.diplom.model;

public enum VehicleType {
    PASSENGER_CAR("Легковой автомобиль"),
    TRUCK("Грузовик"),
    MOTORCYCLE("Мотоцикл"),
    BUS("Автобус"),
    SPECIAL("Спец")
    ;
    public String displayName;

    VehicleType(String displayName) {
        this.displayName = displayName;
    }
}
