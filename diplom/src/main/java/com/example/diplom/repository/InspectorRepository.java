package com.example.diplom.repository;

import com.example.diplom.model.Inspector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectorRepository extends JpaRepository<Inspector ,Long> {
    Inspector findByBadgeNumber(String badgeNumber);
}
