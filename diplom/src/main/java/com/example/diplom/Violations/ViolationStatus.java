package com.example.diplom.Violations;

public enum ViolationStatus {

    CONFIRMED("Подтверждено"),
    REJECTED("Отклонено"),
    PAID("Оплачено"),
    PENDING("В процессе"),
    APPEALED("Обжалаванно ");

    private final String description;

    ViolationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
