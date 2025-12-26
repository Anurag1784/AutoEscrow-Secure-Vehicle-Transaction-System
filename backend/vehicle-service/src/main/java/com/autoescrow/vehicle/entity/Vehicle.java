package com.autoescrow.vehicle.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicle")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(name = "vehicle_year", nullable = false)
    private int year;

    @Column(nullable = false)
    private double price;

    // Seller userId (Auth service)
    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    // 🔥 VERY IMPORTANT (Escrow depends on this)
    @Column(nullable = false)
    private String status; // ACTIVE, SOLD, IN_ESCROW

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();

        // default status
        if (this.status == null) {
            this.status = "ACTIVE";
        }
    }
}
