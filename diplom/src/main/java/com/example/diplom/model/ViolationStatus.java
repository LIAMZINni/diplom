package com.example.diplom.model;

public enum ViolationStatus {
    PENDING("На рассмотрении"),
    CONFIRMED("Подтверждено"),
    REJECTED("Отклонено"),
    NEEDS_REVIEW("На проверке"),
    PAID("Оплачено");

    private final String description;

    ViolationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
