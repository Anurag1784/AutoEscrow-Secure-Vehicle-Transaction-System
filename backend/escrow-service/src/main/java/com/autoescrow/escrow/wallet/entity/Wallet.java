package com.autoescrow.escrow.wallet.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
    name = "wallet",
    uniqueConstraints = @UniqueConstraint(columnNames = "user_email")
)
@Getter
@Setter
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId;

    @Column(name = "user_email", nullable = false, updatable = false)
    private String userEmail;

    @Column(name = "available_balance", nullable = false)
    private BigDecimal availableBalance = BigDecimal.ZERO;

    @Column(name = "locked_balance", nullable = false)
    private BigDecimal lockedBalance = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
