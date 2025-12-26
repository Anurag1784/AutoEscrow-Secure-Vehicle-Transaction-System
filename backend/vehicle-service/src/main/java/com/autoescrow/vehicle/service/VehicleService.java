package com.autoescrow.vehicle.service;

import com.autoescrow.vehicle.entity.Vehicle;
import com.autoescrow.vehicle.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository repository;

    // ===============================
    // ADD VEHICLE
    // ===============================
    public Vehicle addVehicle(Vehicle vehicle) {
        return repository.save(vehicle);
    }

    // ===============================
    // GET ALL VEHICLES
    // ===============================
    public List<Vehicle> getAllVehicles() {
        return repository.findAll();
    }

    // ===============================
    // GET VEHICLE BY ID
    // ===============================
    public Vehicle getVehicleById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Vehicle not found with id: " + id)
                );
    }

    // ===============================
    // 🔥 UPDATE VEHICLE STATUS
    // ===============================
    public Vehicle updateVehicleStatus(Long vehicleId, String status) {

        Vehicle vehicle = repository.findById(vehicleId)
                .orElseThrow(() ->
                        new RuntimeException("Vehicle not found with id: " + vehicleId)
                );

        vehicle.setStatus(status);
        return repository.save(vehicle);
    }
}
