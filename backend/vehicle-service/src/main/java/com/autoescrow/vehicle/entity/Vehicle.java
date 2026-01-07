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

    // 🔐 SELLER EMAIL (USED FOR OWNERSHIP VALIDATION)
    @Column(name = "seller_email", nullable = false)
    private String sellerEmail;

    // (Optional – future use, keep it)
    @Column(name = "seller_id")
    private Long sellerId;

    // Vehicle status
    @Column(nullable = false)
    private String status; // ACTIVE, SOLD

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = "ACTIVE";
        }
    }
}
