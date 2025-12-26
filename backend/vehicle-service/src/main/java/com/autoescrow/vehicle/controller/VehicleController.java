package com.autoescrow.vehicle.controller;

import com.autoescrow.vehicle.entity.Vehicle;
import com.autoescrow.vehicle.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService service;

    // ===============================
    // ADD VEHICLE (SELLER)
    // ===============================
    @PostMapping
    public Vehicle addVehicle(
            @RequestBody Vehicle vehicle,
            Authentication authentication
    ) {
        String username = authentication.getName();

        // Dummy sellerId (later auth-service se map hoga)
        vehicle.setSellerId(1L);
        vehicle.setStatus("ACTIVE");   // safety

        return service.addVehicle(vehicle);
    }

    // ===============================
    // GET ALL VEHICLES
    // ===============================
    @GetMapping
    public List<Vehicle> getVehicles() {
        return service.getAllVehicles();
    }

    // ===============================
    // GET VEHICLE BY ID
    // ===============================
    @GetMapping("/{id}")
    public Vehicle getVehicleById(@PathVariable Long id) {
        return service.getVehicleById(id);
    }

    // ===============================
    // 🔥 UPDATE VEHICLE STATUS (ACTIVE → SOLD)
    // ===============================
    @PutMapping("/{id}/status")
    public Vehicle updateVehicleStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        String status = body.get("status");
        return service.updateVehicleStatus(id, status);
    }
}
