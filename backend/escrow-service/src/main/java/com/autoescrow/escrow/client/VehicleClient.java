package com.autoescrow.escrow.client;

import com.autoescrow.escrow.dto.VehicleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(
        name = "vehicle-service",
        url = "http://localhost:8082",
        configuration = FeignClientConfig.class
)
public interface VehicleClient {

    // ===============================
    // GET VEHICLE BY ID  (JWT REQUIRED)
    // ===============================
    @GetMapping("/api/vehicles/{vehicleId}")
    VehicleResponse getVehicleById(
            @PathVariable Long vehicleId,
            @RequestHeader("Authorization") String authorization
    );

    // ===============================
    // UPDATE VEHICLE STATUS (JWT REQUIRED)
    // ===============================
    @PutMapping("/api/vehicles/{vehicleId}/status")
    void updateVehicleStatus(
            @PathVariable Long vehicleId,
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String authorization
    );
}
