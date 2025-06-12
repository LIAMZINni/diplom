package com.example.diplom.service;


import com.example.diplom.Vehicle.repository.VehicleInfoRepository;
import com.example.diplom.model.*;


import com.example.diplom.repository.CameraReportRepository;
import com.example.diplom.repository.InspectorRepository;
import com.example.diplom.repository.ViolationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ViolationService {

    @Autowired
    private SessionFactory sessionFactory;


    @Autowired
    private  ViolationRepository violationRepository;
    @Autowired
    private  InspectorRepository inspectorRepository;
    @Autowired
    private VehicleInfoRepository vehicleRepository;
    @Autowired
    CameraReportRepository cameraReportRepository;


    @Transactional
    public Violation createFromCameraReport(CameraReport report,String comment,String decision) {
        Violation violation = new Violation();
        violation.setViolationTime(report.getTimestamp());
        violation.setType(report.getViolationType());
        violation.setCameraReport(report);
        violation.setComment(comment);

        // Устанавливаем инспектора
//        Inspector inspector = inspectorRepository.findByUsername(inspectorUsername)
//                .orElseThrow(() -> new IllegalArgumentException("Инспектор не найден"));
//        violation.setInspector(inspector);
        Inspector inspector= inspectorRepository.findByBadgeNumber("1");
        violation.setInspector(inspector);
        System.out.println("222222222"+decision);


        // Устанавливаем ТС, если найдено
        vehicleRepository.findById(report.getPlateNumber())
                .ifPresent(violation::setVehicle);
        System.out.printf(decision);

        // Рассчитываем штраф
//        violation.setFineAmount(fineCalculationService.calculateFine(report));
        switch (decision) {
            case "CONFIRM":
                report.setStatus(CameraReportStatus.VERIFIED);
                violation.setStatus(ViolationStatus.CONFIRMED);
                System.out.println("21111111111111111111111111");





                break;

            case "REJECT":
                report.setStatus(CameraReportStatus.NO_VIOLATION);
                violation.setStatus(ViolationStatus.REJECTED);

                break;

            case "ESCALATE":
                report.setStatus(CameraReportStatus.NEEDS_REVIEW);
                violation.setStatus(ViolationStatus.NEEDS_REVIEW);
                break;
        }
        // Сохраняем нарушение
        Violation savedViolation = violationRepository.save(violation);

        cameraReportRepository.save(report);





        return savedViolation;
    }
    public Map<String, Long> getViolationsByType() {
        return violationRepository.countByType()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> ((ViolationType) tuple[0]).name(),
                        tuple -> (Long) tuple[1]
                ));
    }



    public Map<String, Long> getViolationsByMonth() {
        return violationRepository.countByMonth()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> "Месяц " + tuple[0],
                        tuple -> (Long) tuple[1]
                ));
    }
    public Optional<CameraReport> findNextUnprocessedReport() {
        List<CameraReport> reports = cameraReportRepository.findByStatusOrderByTimestampAsc(
                CameraReportStatus.NEW,
                PageRequest.of(0, 1));
        return reports.isEmpty() ? Optional.empty() : Optional.of(reports.get(0));
    }
//    public long getTodayViolationsCount() {
//        LocalDate today = LocalDate.now();
//        return violationRepository.countByViolationTimeBetween(
//                today.atStartOfDay(),
//                today.atTime(LocalTime.MAX)
//        );
//    }

    public long getPendingVerificationsCount() {
        return cameraReportRepository.countByStatusIn(
                Arrays.asList(
                        CameraReportStatus.NEW,
                        CameraReportStatus.PROCESSING,
                        CameraReportStatus.NEEDS_REVIEW
                )
        );
    }

//    public long getSolvedCasesCount(int days) {
//        LocalDateTime from = LocalDateTime.now().minusDays(days);
//        return violationRepository.countByStatusInAndDecisionTimeBetween(
//                Arrays.asList(
//                        ViolationStatus.CONFIRMED,
//                        ViolationStatus.REJECTED
//                ),
//                from,
//                LocalDateTime.now()
//        );
//    }

    public List<Violation> findRecentViolations(int limit) {
        return violationRepository.findTop5ByOrderByViolationTimeDesc(
                PageRequest.of(0, limit, Sort.by("violationTime").descending())
        );
    }



    /**
     * Добавляет запись о нарушении с фото в БД
     * @param file Файл изображения
     * @param licensePlate Номер авто (формат "А123БВ77")
     * @param recordedSpeed Зафиксированная скорость (км/ч)
     * @param speedLimit Ограничение скорости (км/ч)
     * @param cameraType Тип камеры ("Кордон.Про")
     * @param timestamp Время фиксации ("2023-03-19 11:01:19")
     * @return ID созданной записи
     */
    @Transactional
    public Long addViolationWithPhoto(
            MultipartFile file,
            String licensePlate,
            int recordedSpeed,
            int speedLimit,
            String cameraType,
            String timestamp
    ) throws IOException {

        // 1. Подготовка данных
        String cleanedPlate = licensePlate.replace("#", "").trim();
        LocalDateTime violationTime = parseTimestamp(timestamp);

        // 2. Создание объекта
        ViolationPhoto violation = new ViolationPhoto();
        violation.setFileName(generateFileName(cleanedPlate, recordedSpeed, violationTime));
        violation.setMimeType(file.getContentType());
        violation.setImageData(file.getBytes());
        violation.setLicensePlate(cleanedPlate);
        violation.setRecordedSpeed(recordedSpeed);
        violation.setSpeedLimit(speedLimit);
        violation.setCameraType(cameraType);
        violation.setTimestamp(violationTime);

        // 3. Сохранение в БД
        Session session = sessionFactory.getCurrentSession();
        return (Long) session.save(violation);
    }

    private String generateFileName(String plate, int speed, LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        return String.format("%s_%dkmh_%s.jpg",
                plate,
                speed,
                time.format(formatter)
        );
    }

    private LocalDateTime parseTimestamp(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(timestamp, formatter);
    }



    public ViolationPhoto getPhotoById(Long id) {
        ViolationPhoto photo = sessionFactory.getCurrentSession()
                .get(ViolationPhoto.class, id);


        return photo;
    }

    public int getTotalCount() {
        return violationRepository.findAll().size();
    }
    public List<Map<String, Object>> getHotSpots() {
        return violationRepository.findHotSpots(10); // Топ-10 точек
    }
}