package com.example.diplom.security;

import jakarta.persistence.*;


public enum Role {

    INSPECTOR("Инспектор ДПС"),    // Инспектор ДПС (вносит нарушения)
    ADMIN("Администратор"),        // Администратор (управляет пользователями)

    USER("Пользователь")    //Пользователь
    ;
    private String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }
}
