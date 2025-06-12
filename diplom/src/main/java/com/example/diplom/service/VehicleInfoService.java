package com.example.diplom.service;

import com.example.diplom.Vehicle.VehicleWantedInfo;
import com.example.diplom.Vehicle.repository.VehicleInfoRepository;
import com.example.diplom.Vehicle.repository.WantedVehicleRepository;
import com.example.diplom.model.Vehicle;
import com.example.diplom.model.WantedVehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class VehicleInfoService {
    @Autowired
    VehicleInfoRepository vehicleInfoRepository;
    public Vehicle getVehicleInfo(String plateNumber) {


        // 1. Получение базовой информации о ТС
        Optional<Vehicle> gibddInfo = vehicleInfoRepository.findById(plateNumber);
        if (gibddInfo.isPresent()) {


        }



        return gibddInfo.orElse(null);
    }
    @Autowired
    private WantedVehicleRepository wantedRepo;

    public VehicleWantedInfo getFullInfoByPlate(String plateNumber) {
        Vehicle vehicle = vehicleInfoRepository.findById(plateNumber).orElse(null);
        WantedVehicle wanted = wantedRepo.findByPlateNumber(plateNumber).orElse(null);

        return new VehicleWantedInfo(vehicle, wanted);
    }




}
