package com.example.diplom.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Data
@Entity
@Getter
@Setter

@AllArgsConstructor
@Builder
public class CameraReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cameraReportId;

    @ManyToOne
    @JoinColumn(name = "camera_id", nullable = false,referencedColumnName = "camera_id")
    private Camera camera;      // "КРИС-С #SK11"
    private String plateNumber;   // Госномер (например, "А123БВ77")
    private LocalDateTime timestamp;
    private Integer detectedSpeed; // 145 км/ч
    private Integer speedLimit;   // 110 км/ч
    private String location;      // "г. СПб, 77 км 790 м КАД..."
    private String direction;     // "встречная"
    private String lane;// "крайняя левая полоса"

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] imageData;

    @Column( length = 100)
    private String imageMimeType; // "image/jpeg"

    @Column(length = 255)
    private String imageFileName;

    @Enumerated(EnumType.STRING)
    private ViolationType violationType;//

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CameraReportStatus status = CameraReportStatus.NEW;

    public CameraReport() {
    }

    public String generateFileName() {
        return String.format("%s_%dkmh_%s.%s",
                plateNumber,
                detectedSpeed,
                timestamp.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")),
                getFileExtension()
        );
    }
    private String getFileExtension() {
        return switch (imageMimeType) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            default -> "bin";
        };
    }


}
