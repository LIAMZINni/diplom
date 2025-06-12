package com.example.diplom.Alerts;

import lombok.Data;


public enum Priority {
    LOW("Низкий"), MEDIUM("Средний"), HIGH("Высокий"), URGENT("Срочный");
    private final String displayName;


    Priority(String displayName) {
        this.displayName = displayName;
    }
}
