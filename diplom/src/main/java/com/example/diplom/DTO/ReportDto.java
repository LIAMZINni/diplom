package com.example.diplom.DTO;
import com.example.diplom.model.ViolationType;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ReportDto {
    private Long id;
    private String plateNumber;
    private LocalDateTime timestamp;
    private Integer detectedSpeed;
    private Integer speedLimit;
    private String location;
    private String direction;
    private String lane;
    private ViolationType violationType;
    private String cameraModel;
    private String imageUrl; // URL для получения изображения
}