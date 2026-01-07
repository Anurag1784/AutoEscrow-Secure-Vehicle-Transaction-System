package com.autoescrow.escrow.wallet.dto;

import java.math.BigDecimal;

public class WalletTopUpRequest {

    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
