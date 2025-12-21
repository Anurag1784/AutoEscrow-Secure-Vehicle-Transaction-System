package com.autoescrow.escrow.service;

import java.math.BigDecimal;

import com.autoescrow.escrow.entity.EscrowTransaction;

public interface EscrowService {

    EscrowTransaction createEscrow(
            String buyerEmail,
            String sellerEmail,
            Long vehicleId,
            BigDecimal amount
    );

    EscrowTransaction sellerConfirm(Long escrowId, String sellerEmail);

    EscrowTransaction buyerConfirm(Long escrowId, String buyerEmail);

    EscrowTransaction getEscrowById(Long escrowId);
}
