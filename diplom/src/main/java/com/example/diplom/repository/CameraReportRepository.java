package com.example.diplom.repository;

import com.example.diplom.model.CameraReport;
import com.example.diplom.model.CameraReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CameraReportRepository extends JpaRepository<CameraReport,Long>, JpaSpecificationExecutor<CameraReport> {
    @Query("SELECT cr FROM CameraReport cr LEFT JOIN FETCH cr.camera WHERE cr.camera.cameraId = :cameraId OR :cameraId IS NULL")
    List<CameraReport> findByFilters(
            @Param("cameraId") String cameraId,
            @Param("plateNumber") String plateNumber,
            @Param("date") LocalDate date);

    @Query("SELECT cr FROM CameraReport cr LEFT JOIN FETCH cr.camera WHERE cr.cameraReportId = :id")
    Optional<CameraReport> findByIdWithCamera(@Param("id") Long id);

    @Query("SELECT cr FROM CameraReport cr LEFT JOIN FETCH cr.camera")
    List<CameraReport> findAllWithCamera();

    Optional<CameraReport> findFirstByStatusOrderByTimestampAsc(CameraReportStatus status);

    @Query("SELECT cr FROM CameraReport cr WHERE cr.status = :status ORDER BY cr.timestamp ASC")
    List<CameraReport> findByStatusOrderByTimestampAsc(
            @Param("status") CameraReportStatus status,
            Pageable pageable);

    long countByStatusIn(List<CameraReportStatus> list);

    @Query("SELECT r.status, COUNT(r) FROM CameraReport r GROUP BY r.status")
    List<Object[]> countByStatus();

    @Query("SELECT r.location, COUNT(r) FROM CameraReport r GROUP BY r.location ORDER BY COUNT(r) DESC")
    List<Object[]> countByLocation();
    @Query("SELECT r.camera, r.camera.modelName, COUNT(r) " +
            "FROM CameraReport r GROUP BY r.camera, r.camera.modelName ORDER BY COUNT(r) DESC")
    List<Object[]> countByCamera();


}
