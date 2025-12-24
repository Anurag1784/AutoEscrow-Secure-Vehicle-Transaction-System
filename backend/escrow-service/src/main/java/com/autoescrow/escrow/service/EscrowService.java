package com.autoescrow.escrow.service;

import java.math.BigDecimal;
import java.util.List;

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

    // Buyer-initiated cancel escrow
    EscrowTransaction cancelEscrow(Long escrowId, String buyerEmail);

    // ADMIN: Force cancel escrow
    EscrowTransaction adminForceCancel(Long escrowId);

    // ADMIN: Force refund escrow
    EscrowTransaction adminForceRefund(Long escrowId);

    // ADMIN: View all escrows
    List<EscrowTransaction> getAllEscrows();
}
