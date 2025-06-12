package com.example.diplom.Vehicle.repository;

import com.example.diplom.model.WantedVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WantedVehicleRepository extends JpaRepository<WantedVehicle ,Long> {

    Optional<WantedVehicle> findByPlateNumber(String plateNumber);

    @Query("SELECT w FROM WantedVehicle w WHERE w.isActive = true")
    List<WantedVehicle> findAllActive();



}
