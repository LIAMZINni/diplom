package com.example.diplom.model;

public enum AlertType {
    STOLEN("Угон ТС"),
    WANTED("Розыск водителя"),
    WRONG_PLATE("Несоответствие номера"),
    DANGEROUS("Опасный водитель"),
    OTHER("Другое");

    private final String displayName;

    AlertType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
