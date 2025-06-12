package com.example.diplom.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Data
@Entity
@Getter
@Setter
@Table(name = "violation_photos")
public class ViolationPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Основные данные изображения
    @Column(nullable = false)
    private String fileName;          // Пример: "speed_153_kmh.jpg"

    @Column(nullable = false)
    private String mimeType;          // "image/jpeg"

    @Lob
    @Column(columnDefinition = "LONGBLOB", nullable = false)
    private byte[] imageData;         // Бинарные данные фото

    // Метаданные из примера
    private String licensePlate;      // "М#РМ0111" (убрать решётку при сохранении)
    private int recordedSpeed;        // 153 (км/ч)
    private int speedLimit;           // 60 (км/ч)
    private String cameraType;        // "Кордон.Про"
    private String direction;         // "попутное"
    private String location;          // "Пензенская область, г. Пенза, ул. Центральная д. 1"
    private LocalDateTime timestamp;  // 2023-03-19T11:01:19

    // Автоматически заполняемые поля
    private LocalDateTime createdAt;

    public void setId(Long id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setRecordedSpeed(int recordedSpeed) {
        this.recordedSpeed = recordedSpeed;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    public void setCameraType(String cameraType) {
        this.cameraType = cameraType;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public int getRecordedSpeed() {
        return recordedSpeed;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public String getCameraType() {
        return cameraType;
    }

    public String getDirection() {
        return direction;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
