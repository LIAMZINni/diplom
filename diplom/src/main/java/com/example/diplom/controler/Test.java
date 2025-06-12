package com.example.diplom.controler;

import com.example.diplom.model.Camera;
import com.example.diplom.model.CameraReport;
import com.example.diplom.model.ViolationType;
import com.example.diplom.repository.CameraReportRepository;
import com.example.diplom.repository.CameraRepository;
import com.example.diplom.service.ViolationService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
public class Test {

    @Autowired
    ViolationService violationService;
    @Autowired
    private  CameraReportRepository cameraReportRepository;
    @Autowired
    private  CameraRepository cameraRepository;
    @Transactional
    public void uploadManually() throws IOException {
        try {
            // 1. Загрузка файла
            Path imagePath = Paths.get("D:\\загрузки\\77c1aaes-960.jpg");
            if (!Files.exists(imagePath)) {
                throw new FileNotFoundException("Файл изображения не найден: " + imagePath);
            }

            byte[] imageData = Files.readAllBytes(imagePath);
            String mimeType = Files.probeContentType(imagePath);
            if (mimeType == null) {
                mimeType = "image/jpeg"; // fallback
            }

            // 2. Подготовка MultipartFile
            MultipartFile file = new MockMultipartFile(
                    "violation_image",
                    imagePath.getFileName().toString(),
                    mimeType,
                    imageData
            );

            // 3. Поиск камеры (или создание новой)
            Camera camera = cameraRepository.findById((long) 1L)
                    .orElseGet(() -> {
                        Camera newCamera = new Camera();
                        newCamera.setCameraId(2L);
                        newCamera.setModelName("Кордон-М2");
                        return (Camera) cameraRepository.save(newCamera);
                    });

            // 4. Создание и сохранение отчета
            CameraReport report = CameraReport.builder()
                    .camera(camera)
                    .plateNumber("Н722СУ98")
                    .timestamp(LocalDateTime.parse("2025-03-19T11:01:19"))
                    .detectedSpeed(85)
                    .speedLimit(60)
                    .location("Воронежская  область, г. Воронеж, ул. Центральная д. 1")
                    .direction("встречное ")
                    .lane("основная полоса")
                    .imageData(file.getBytes())
                    .imageMimeType(file.getContentType())
                    .violationType(ViolationType.SPEEDING)
                    .build();

            report.setImageFileName(report.generateFileName());

            CameraReport savedReport = cameraReportRepository.save(report);
            System.out.printf("Нарушение сохранено. ID: %d, Файл: %s%n",
                    savedReport.getCameraReportId(),
                    savedReport.getImageFileName());

        } catch (FileNotFoundException e) {
            System.err.println("Ошибка: Файл не найден. " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Неожиданная ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

        // 1. Загрузка файла (укажите реальный путь)
//        Path imagePath = Paths.get("D:\\загрузки\\diplom\\diplom\\viol.jpg");
//        byte[] imageData = Files.readAllBytes(imagePath);
//
//        // 2. Создание временного файла для MultipartFile
//        MultipartFile file = new MockMultipartFile(
//                "viol.jpg",             // имя файла
//                "viol.jpg",             // оригинальное имя
//                "image/jpeg",                // MIME-тип
//                imageData                    // данные
//        );
//
//        // 3. Вызов метода сохранения
//        Long violationId = violationService.addViolationWithPhoto(
//                file,
//                "МРМ0111",                   // номер авто
//                153,                         // скорость
//                60,                          // лимит
//                "Кордон.Про",                // тип камеры
//                "2023-03-19 11:01:19"        // дата/время
//        );
//
//        System.out.println("Нарушение сохранено. ID: " + violationId);
//    }
}