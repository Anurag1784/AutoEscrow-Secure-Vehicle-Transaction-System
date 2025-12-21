package com.autoescrow.escrow.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "escrow_transaction")
@Getter
@Setter
public class EscrowTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long escrowId;

    private String buyerEmail;
    private String sellerEmail;

    private Long vehicleId;
    private BigDecimal amount;

    private String status;

    private boolean sellerConfirmed;
    private boolean buyerConfirmed;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
