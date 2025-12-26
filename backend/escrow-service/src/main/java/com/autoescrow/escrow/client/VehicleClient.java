package com.autoescrow.escrow.client;

import com.autoescrow.escrow.dto.VehicleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(
        name = "vehicle-service",
        url = "http://localhost:8082",
        configuration = FeignClientConfig.class
)
public interface VehicleClient {

    // ===============================
    // GET VEHICLE BY ID
    // ===============================
    @GetMapping("/api/vehicles/{vehicleId}")
    VehicleResponse getVehicleById(@PathVariable Long vehicleId);

    // ===============================
    // 🔥 UPDATE VEHICLE STATUS (ACTIVE → SOLD)
    // ===============================
    @PutMapping("/api/vehicles/{vehicleId}/status")
    void updateVehicleStatus(
            @PathVariable Long vehicleId,
            @RequestBody Map<String, String> body
    );
}
