package com.autoescrow.escrow.wallet.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.autoescrow.escrow.wallet.enums.WalletTransactionType;

import jakarta.persistence.*;

@Entity
@Table(name = "wallet_transaction")
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Wallet owner
    @Column(name = "user_email", nullable = false)
    private String userEmail;

    // Always POSITIVE amount
    @Column(nullable = false)
    private BigDecimal amount;

    // CREDIT / LOCK / RELEASE / REFUND etc.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WalletTransactionType type;

    // Balance snapshot AFTER transaction
    @Column(name = "balance_after", nullable = false)
    private BigDecimal balanceAfter;

    // Optional link to escrow
    @Column(name = "escrow_id")
    private Long escrowId;

    // Human-readable explanation
    @Column(length = 255)
    private String description;

    // Transaction timestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // =====================
    // Getters & Setters
    // =====================

    public Long getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public WalletTransactionType getType() {
        return type;
    }
    public void setType(WalletTransactionType type) {
        this.type = type;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }
    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public Long getEscrowId() {
        return escrowId;
    }
    public void setEscrowId(Long escrowId) {
        this.escrowId = escrowId;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
