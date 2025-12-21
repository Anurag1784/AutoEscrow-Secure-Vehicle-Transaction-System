package com.autoescrow.escrow.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        escrow.setStatus("FUNDS_DEPOSITED");

        return repository.save(escrow);
    }

    @Override
    public EscrowTransaction sellerConfirm(Long escrowId, String sellerEmail) {
        EscrowTransaction escrow = repository.findById(escrowId)
                .orElseThrow();

        if (!escrow.getSellerEmail().equals(sellerEmail)) {
            throw new RuntimeException("Unauthorized seller");
        }

        escrow.setSellerConfirmed(true);
        escrow.setStatus("SELLER_CONFIRMED");

        if (escrow.isBuyerConfirmed()) {
            escrow.setStatus("COMPLETED");
        }

        return repository.save(escrow);
    }

    @Override
    public EscrowTransaction buyerConfirm(Long escrowId, String buyerEmail) {
        EscrowTransaction escrow = repository.findById(escrowId)
                .orElseThrow();

        if (!escrow.getBuyerEmail().equals(buyerEmail)) {
            throw new RuntimeException("Unauthorized buyer");
        }

        escrow.setBuyerConfirmed(true);
        escrow.setStatus("BUYER_CONFIRMED");

        if (escrow.isSellerConfirmed()) {
            escrow.setStatus("COMPLETED");
        }

        return repository.save(escrow);
    }

    @Override
    public EscrowTransaction getEscrowById(Long escrowId) {
        return repository.findById(escrowId).orElseThrow();
    }
}
