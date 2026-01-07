package com.autoescrow.escrow.wallet.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.autoescrow.escrow.wallet.enums.WalletTransactionType;

public class WalletTransactionDTO {

    private BigDecimal amount;
    private WalletTransactionType type;
    private BigDecimal balanceAfter;
    private String description;
    private Long escrowId;
    private LocalDateTime createdAt;

    public WalletTransactionDTO(
            BigDecimal amount,
            WalletTransactionType type,
            BigDecimal balanceAfter,
            String description,
            Long escrowId,
            LocalDateTime createdAt
    ) {
        this.amount = amount;
        this.type = type;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.escrowId = escrowId;
        this.createdAt = createdAt;
    }

    // getters
    public BigDecimal getAmount() { return amount; }
    public WalletTransactionType getType() { return type; }
    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public String getDescription() { return description; }
    public Long getEscrowId() { return escrowId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
