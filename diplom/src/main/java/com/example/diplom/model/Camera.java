package com.example.diplom.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Entity
public class Camera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "camera_id")
    private Long cameraId;


    private String modelName;


    private String gpsCoordinates; // Формат "55.12345,37.12345"

    @Enumerated(EnumType.STRING)
    private CameraType cameraType;

    @OneToMany(mappedBy = "camera")
    private List<CameraReport> reports;

}
