package com.example.diplom.Vehicle.repository;

import com.example.diplom.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleInfoRepository extends JpaRepository<Vehicle,String> {


}
