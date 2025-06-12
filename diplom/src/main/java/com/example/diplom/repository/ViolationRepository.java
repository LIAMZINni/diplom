package com.example.diplom.repository;

import com.example.diplom.model.Violation;
import com.example.diplom.model.ViolationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface ViolationRepository extends JpaRepository<Violation,Long> {
    @Query("SELECT v.type, COUNT(v) FROM Violation v GROUP BY v.type")
    List<Object[]> countByType();

    @Query("SELECT MONTH(v.violationTime), COUNT(v) FROM Violation v GROUP BY MONTH(v.violationTime)")
    List<Object[]> countByMonth();

    List<Violation> findTop5ByOrderByViolationTimeDesc(Pageable pageable);

    @Query("SELECT v.cameraReport.location as location, COUNT(v) as count, " +
            "AVG(v.cameraReport.detectedSpeed) as avgSpeed " +
            "FROM Violation v GROUP BY v.cameraReport.location ORDER BY COUNT(v) DESC LIMIT :limit")
    List<Map<String, Object>> findHotSpots(@Param("limit") int limit);
}
