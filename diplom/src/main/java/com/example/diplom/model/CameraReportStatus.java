package com.example.diplom.model;

import lombok.Data;


public enum CameraReportStatus {
    NEW("Новый"),               // Только получен от камеры
    PROCESSING("В обработке"),  // В процессе анализа
    VERIFIED("Нарушение обнаружено"),
    NO_VIOLATION("Нарушений нет"),
    ERROR("Ошибка обработки"), NEEDS_REVIEW("");

    public final String description;

    CameraReportStatus(String description) {
        this.description = description;
    }
}
