package com.autoescrow.escrow.wallet.enums;

public enum WalletTransactionType {

    CREDIT,        // Wallet top-up
    DEBIT,         // Future use (manual debit)
    LOCK,          // Amount locked for escrow
    RELEASE,       // Released to seller
    REFUND,        // Refunded to buyer
    ADJUSTMENT     // Admin/system correction
}
