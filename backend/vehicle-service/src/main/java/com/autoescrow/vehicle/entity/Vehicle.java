package com.autoescrow.vehicle.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;

    private String brand;
    private String model;
    private int year;
    private double price;

    // Auth Service se aane wala userId (future use)
    private Long sellerId;
    
    
}
