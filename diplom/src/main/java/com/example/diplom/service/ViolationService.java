package com.example.diplom.service;


import com.example.diplom.Vehicle.repository.VehicleInfoRepository;
import com.example.diplom.Violations.PdfGenerator;
import com.example.diplom.Violations.Violation;
import com.example.diplom.Violations.ViolationStatus;
import com.example.diplom.model.*;


import com.example.diplom.repository.CameraReportRepository;
import com.example.diplom.repository.InspectorRepository;
import com.example.diplom.Violations.ViolationRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
    @Autowired
    PdfGenerator pdfGenerator;

    @Transactional
    public Violation createFromCameraReport(CameraReport report, String comment, String decision) {
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


        switch (decision) {
            case "CONFIRM":
                report.setStatus(CameraReportStatus.VERIFIED);
                violation.setStatus(ViolationStatus.CONFIRMED);






                break;

            case "REJECT":
                report.setStatus(CameraReportStatus.NO_VIOLATION);
//                violation.setStatus(ViolationStatus.REJECTED);

                break;

            case "ESCALATE":
                report.setStatus(CameraReportStatus.NEEDS_REVIEW);
//                violation.setStatus(ViolationStatus.NEEDS_REVIEW);
                break;
        }
        // Сохраняем нарушение
        Violation savedViolation = violationRepository.save(violation);

        cameraReportRepository.save(report);





        return savedViolation;
    }
    public Page<Violation> getFines(
            ViolationStatus status,
            String plateNumber,
            LocalDate startDate,
            LocalDate endDate,
            int page,
            String sort) {

        Specification<Violation> spec = Specification.where(null);

        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        if (plateNumber != null && !plateNumber.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.upper(root.get("vehicle").get("plateNumber")),
                            "%" + plateNumber.toUpperCase() + "%"));
        }

        if (startDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("violationTime"), startDate.atStartOfDay()));
        }

        if (endDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("violationTime"), endDate.atTime(23, 59, 59)));
        }

        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                page,
                20,
                Sort.by(direction, sortParams[0]));

        return violationRepository.findAll(spec, pageable);
    }
    public Map<String, Long> getViolationsByType() {
        return violationRepository.countByType()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> ((ViolationType) tuple[0]).getDisplayName(),
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


    public long getPendingVerificationsCount() {
        return cameraReportRepository.countByStatusIn(
                Arrays.asList(
                        CameraReportStatus.NEW,
                        CameraReportStatus.PROCESSING,
                        CameraReportStatus.NEEDS_REVIEW
                )
        );
    }
    public byte[] generatePrescriptionPdf(Long violationId) throws Exception {
        Violation violation = violationRepository.findById(violationId)
                .orElseThrow(() -> new Exception("Violation not found"));

        if (violation.getPrescriptionPdf() == null) {
            violation.setPrescriptionPdf(pdfGenerator.generatePrescription(violation));
            violationRepository.save(violation);
        }

        return violation.getPrescriptionPdf();
    }





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

    public Optional<Violation> getById(Long id) {
       return violationRepository.findById(id);
    }
}