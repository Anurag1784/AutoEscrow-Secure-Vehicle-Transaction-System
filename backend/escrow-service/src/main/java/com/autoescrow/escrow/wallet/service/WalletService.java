package com.autoescrow.escrow.wallet.service;

import java.math.BigDecimal;

public interface WalletService {

    void createWalletIfNotExists(String userEmail);

    void creditAvailable(String userEmail, BigDecimal amount);

    void lockFunds(String userEmail, BigDecimal amount);

    void releaseLockedToSeller(
            String buyerEmail,
            String sellerEmail,
            BigDecimal amount
    );

    void refundLockedToBuyer(String buyerEmail, BigDecimal amount);
}
