package com.autoescrow.escrow.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autoescrow.escrow.entity.EscrowTransaction;
import com.autoescrow.escrow.repository.EscrowTransactionRepository;

@Service
public class EscrowServiceImpl implements EscrowService {

    @Autowired
    private EscrowTransactionRepository repository;

    @Override
    public EscrowTransaction createEscrow(
            String buyerEmail,
            String sellerEmail,
            Long vehicleId,
            BigDecimal amount) {

        EscrowTransaction escrow = new EscrowTransaction();

        escrow.setBuyerEmail(buyerEmail);
        escrow.setSellerEmail(sellerEmail);
        escrow.setVehicleId(vehicleId);
        escrow.setAmount(amount);

        // creation timestamp
        escrow.setCreatedAt(LocalDateTime.now());

        // seller must confirm within 24 hours
        escrow.setSellerConfirmDeadline(
                LocalDateTime.now().plusHours(24)
        );

        // initial status
        escrow.setStatus("FUNDS_DEPOSITED");

        escrow.setSellerConfirmed(false);
        escrow.setBuyerConfirmed(false);

        return repository.save(escrow);
    }

    @Override
    public EscrowTransaction sellerConfirm(Long escrowId, String sellerEmail) {

        EscrowTransaction escrow = repository.findById(escrowId)
                .orElseThrow(() -> new RuntimeException("Escrow not found"));

        if (!escrow.getSellerEmail().equals(sellerEmail)) {
            throw new RuntimeException("Unauthorized seller");
        }

        escrow.setSellerConfirmed(true);
        escrow.setStatus("SELLER_CONFIRMED");
        escrow.setUpdatedAt(LocalDateTime.now());

        if (escrow.isBuyerConfirmed()) {
            escrow.setStatus("COMPLETED");
            escrow.setCompletedAt(LocalDateTime.now());
        }

        return repository.save(escrow);
    }

    @Override
    public EscrowTransaction buyerConfirm(Long escrowId, String buyerEmail) {

        EscrowTransaction escrow = repository.findById(escrowId)
                .orElseThrow(() -> new RuntimeException("Escrow not found"));

        if (!escrow.getBuyerEmail().equals(buyerEmail)) {
            throw new RuntimeException("Unauthorized buyer");
        }

        if (!escrow.isSellerConfirmed()) {
            throw new RuntimeException("Seller has not confirmed yet");
        }

        escrow.setBuyerConfirmed(true);
        escrow.setStatus("COMPLETED");
        escrow.setCompletedAt(LocalDateTime.now());

        return repository.save(escrow);
    }

    @Override
    public EscrowTransaction getEscrowById(Long escrowId) {
        return repository.findById(escrowId)
                .orElseThrow(() -> new RuntimeException("Escrow not found"));
    }

    // ==================================================
    // STEP 2: Buyer-Initiated Cancel Escrow
    // ==================================================
    @Override
    @Transactional
    public EscrowTransaction cancelEscrow(Long escrowId, String buyerEmail) {

        EscrowTransaction escrow = repository.findById(escrowId)
                .orElseThrow(() -> new RuntimeException("Escrow not found"));

        // buyer authorization
        if (!escrow.getBuyerEmail().equals(buyerEmail)) {
            throw new RuntimeException("Unauthorized buyer");
        }

        // allow cancel ONLY before seller confirmation
        if (!"FUNDS_DEPOSITED".equals(escrow.getStatus())) {
            throw new RuntimeException(
                    "Escrow cannot be cancelled in current state: "
                            + escrow.getStatus()
            );
        }

        // mark as refunded
        escrow.setStatus("REFUNDED");
        escrow.setCompletedAt(LocalDateTime.now());

        return repository.save(escrow);
    }
}
