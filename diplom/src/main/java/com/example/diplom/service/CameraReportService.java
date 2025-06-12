package com.example.diplom.service;

import com.example.diplom.model.Camera;
import com.example.diplom.model.CameraReport;
import com.example.diplom.model.CameraReportStatus;
import com.example.diplom.repository.CameraReportRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CameraReportService {
    @Autowired
    private CameraReportRepository reportRepository;



    public Page<CameraReport> getAllReports(Pageable pageable) {
        return reportRepository.findAll(pageable);
    }

    public Optional<CameraReport> getReportById(Long id) {
        return reportRepository.findById(id);
    }

    public CameraReport saveReport(CameraReport report) {
        return reportRepository.save(report);
    }

    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
    public Map<String, Long> getReportsByStatus() {
        return reportRepository.countByStatus()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> ((CameraReportStatus) tuple[0]).name(),
                        tuple -> (Long) tuple[1]
                ));
    }

    public int getTotalCount() {
       return reportRepository.findAll().size();
    }
    public Map<String, Long> getViolationsByLocation() {
        return reportRepository.countByLocation()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> (String) tuple[0], // location
                        tuple -> (Long) tuple[1]    // count
                ));
    }
    public Map<String, Long> getViolationsByCamera() {
        return reportRepository.countByCamera()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> ((Camera) tuple[0]).getModelName() + " (" + tuple[1] + ")",
                        tuple -> (Long) tuple[2]
                ));
    }

    public Page<CameraReport> getFilteredReports(
            Long cameraId,
            String cameraModel,
            String plateNumber,
            CameraReportStatus status,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable) {

        Specification<CameraReport> spec = Specification.where(null);

        if (cameraId != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("camera").get("cameraId"), cameraId));
        }

        if (cameraModel != null && !cameraModel.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("camera").get("modelName")),
                            "%" + cameraModel.toLowerCase() + "%"));
        }

        if (plateNumber != null && !plateNumber.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.upper(root.get("plateNumber")),
                            "%" + plateNumber.toUpperCase() + "%"));
        }

        // Существующие фильтры по статусу и дате
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        if (startDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("timestamp"), startDate.atStartOfDay()));
        }

        if (endDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("timestamp"), endDate.atTime(23, 59, 59)));
        }

        return reportRepository.findAll(spec, pageable);
    }
}
