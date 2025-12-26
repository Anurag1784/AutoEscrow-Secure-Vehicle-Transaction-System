package com.autoescrow.escrow.service;

import java.math.BigDecimal;
import java.util.List;

import com.autoescrow.escrow.entity.EscrowTransaction;

public interface EscrowService {

    // ===============================
    // CREATE ESCROW (BUYER)
    // ===============================
    EscrowTransaction createEscrow(
            String buyerEmail,
            String sellerEmail,
            Long vehicleId,
            BigDecimal amount
    );

    // ===============================
    // SELLER CONFIRM
    // ===============================
    EscrowTransaction sellerConfirm(Long escrowId, String sellerEmail);

    // ===============================
    // BUYER CONFIRM
    // ===============================
    EscrowTransaction buyerConfirm(Long escrowId, String buyerEmail);

    // ===============================
    // GET ESCROW BY ID
    // ===============================
    EscrowTransaction getEscrowById(Long escrowId);

    // ===============================
    // BUYER CANCEL ESCROW
    // ===============================
    EscrowTransaction cancelEscrow(Long escrowId, String buyerEmail);

    // ===============================
    // ADMIN FORCE CANCEL
    // ===============================
    EscrowTransaction adminForceCancel(Long escrowId);

    // ===============================
    // ADMIN FORCE REFUND
    // ===============================
    EscrowTransaction adminForceRefund(Long escrowId);

    // ===============================
    // ADMIN VIEW ALL ESCROWS
    // ===============================
    List<EscrowTransaction> getAllEscrows();
}
