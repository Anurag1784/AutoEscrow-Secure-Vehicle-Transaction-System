package com.autoescrow.escrow.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.autoescrow.escrow.state.EscrowStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    /**
     * Escrow status (STATE MACHINE BASE)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EscrowStatus status;

    /**
     * Manual confirmations
     */
    private boolean sellerConfirmed;
    private boolean buyerConfirmed;

    /**
     * Time tracking fields (IMPORTANT for timeout & refund)
     */
    private LocalDateTime createdAt;

    // Seller must confirm before this time
    private LocalDateTime sellerConfirmDeadline;

    // When escrow is completed / refunded
    private LocalDateTime completedAt;

    // Optional: track updates
    private LocalDateTime updatedAt;
}
