package com.autoescrow.vehicle.controller;

import com.autoescrow.vehicle.entity.Vehicle;
import com.autoescrow.vehicle.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService service;

    @PostMapping
    public Vehicle addVehicle(
            @RequestBody Vehicle vehicle,
            Authentication authentication
    ) {
        // JWT se username mil raha hai
        String username = authentication.getName();

        // abhi dummy sellerId
        vehicle.setSellerId(1L);

        return service.addVehicle(vehicle);
    }

    @GetMapping
    public List<Vehicle> getVehicles() {
        return service.getAllVehicles();
    }
}
