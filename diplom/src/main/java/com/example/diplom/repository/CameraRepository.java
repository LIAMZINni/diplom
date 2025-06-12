package com.example.diplom.repository;

import com.example.diplom.model.Camera;
import com.example.diplom.model.CameraReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CameraRepository extends JpaRepository<Camera,Long> {


}
