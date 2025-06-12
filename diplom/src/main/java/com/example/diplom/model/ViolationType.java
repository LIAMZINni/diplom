package com.example.diplom.model;

import jakarta.persistence.Entity;


public enum ViolationType {
    SPEEDING("Превышение скорости")                   // Превышение скорости
//    RED_LIGHT,                  // Проезд на красный
//    PARKING_VIOLATION,          // Нарушение правил парковки
//    SEATBELT,                   // Непристегнутый ремень
//    DRUNK_DRIVING,              // Вождение в нетрезвом виде
//    NO_INSURANCE,               // Отсутствие страховки
//    WRONG_WAY,                  // Движение по встречной полосе
//    PHONE_USAGE,                // Использование телефона
//    TECHNICAL_DEFECT,           // Техническая неисправность
//    DOCUMENTS,                  // Проблемы с документами
//    OTHER                       // Иные нарушения
    ;
    public final   String displayName;

    ViolationType(String displayName) {
        this.displayName=displayName;
    }
}

