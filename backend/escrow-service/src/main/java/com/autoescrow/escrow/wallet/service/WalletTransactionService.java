package com.autoescrow.escrow.wallet.service;

import java.math.BigDecimal;

import com.autoescrow.escrow.wallet.enums.WalletTransactionType;

public interface WalletTransactionService {

    void recordTransaction(
            String userEmail,
            BigDecimal amount,
            WalletTransactionType type,
            BigDecimal balanceAfter,
            Long escrowId,
            String description
    );
}
