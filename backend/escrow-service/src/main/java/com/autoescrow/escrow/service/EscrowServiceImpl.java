package com.autoescrow.escrow.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autoescrow.escrow.audit.AdminAuditService;
import com.autoescrow.escrow.entity.EscrowTransaction;
import com.autoescrow.escrow.exception.EscrowNotFoundException;
import com.autoescrow.escrow.exception.InvalidEscrowStateException;
import com.autoescrow.escrow.exception.UnauthorizedActionException;
import com.autoescrow.escrow.repository.EscrowTransactionRepository;

import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class EscrowServiceImpl implements EscrowService {

    @Autowired
    private EscrowTransactionRepository repository;

    @Autowired
    private AdminAuditService auditService;

    // ===============================
    // CREATE ESCROW (BUYER)
    // ===============================
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

        escrow.setCreatedAt(LocalDateTime.now());
        escrow.setSellerConfirmDeadline(
                LocalDateTime.now().plusHours(24)
        );

        escrow.setStatus("FUNDS_DEPOSITED");
        escrow.setSellerConfirmed(false);
        escrow.setBuyerConfirmed(false);

        return repository.save(escrow);
    }

    // ===============================
    // SELLER CONFIRM
    // ===============================
    @Override
    public EscrowTransaction sellerConfirm(Long escrowId, String sellerEmail) {

        EscrowTransaction escrow = repository.findById(escrowId)
                .orElseThrow(() ->
                        new EscrowNotFoundException("Escrow not found"));

        if (!escrow.getSellerEmail().equals(sellerEmail)) {
            throw new UnauthorizedActionException("Unauthorized seller");
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

    // ===============================
    // BUYER CONFIRM
    // ===============================
    @Override
    public EscrowTransaction buyerConfirm(Long escrowId, String buyerEmail) {

        EscrowTransaction escrow = repository.findById(escrowId)
                .orElseThrow(() ->
                        new EscrowNotFoundException("Escrow not found"));

        if (!escrow.getBuyerEmail().equals(buyerEmail)) {
            throw new UnauthorizedActionException("Unauthorized buyer");
        }

        if (!escrow.isSellerConfirmed()) {
            throw new InvalidEscrowStateException(
                    "Seller has not confirmed yet");
        }

        escrow.setBuyerConfirmed(true);
        escrow.setStatus("COMPLETED");
        escrow.setCompletedAt(LocalDateTime.now());

        return repository.save(escrow);
    }

    // ===============================
    // GET ESCROW BY ID
    // ===============================
    @Override
    public EscrowTransaction getEscrowById(Long escrowId) {
        return repository.findById(escrowId)
                .orElseThrow(() ->
                        new EscrowNotFoundException("Escrow not found"));
    }

    // ===============================
    // BUYER CANCEL ESCROW
    // ===============================
    @Override
    @Transactional
    public EscrowTransaction cancelEscrow(Long escrowId, String buyerEmail) {

        EscrowTransaction escrow = repository.findById(escrowId)
                .orElseThrow(() ->
                        new EscrowNotFoundException("Escrow not found"));

        if (!escrow.getBuyerEmail().equals(buyerEmail)) {
            throw new UnauthorizedActionException("Unauthorized buyer");
        }

        if (!"FUNDS_DEPOSITED".equals(escrow.getStatus())) {
            throw new InvalidEscrowStateException(
                    "Escrow cannot be cancelled in state: "
                            + escrow.getStatus()
            );
        }

        escrow.setStatus("REFUNDED");
        escrow.setCompletedAt(LocalDateTime.now());

        return repository.save(escrow);
    }

    // ===============================
    // ADMIN FORCE CANCEL
    // ===============================
    @Override
    public EscrowTransaction adminForceCancel(Long escrowId) {

        EscrowTransaction escrow = repository.findById(escrowId)
                .orElseThrow(() ->
                        new EscrowNotFoundException("Escrow not found"));

        if ("COMPLETED".equals(escrow.getStatus()) ||
            "REFUNDED".equals(escrow.getStatus())) {

            throw new InvalidEscrowStateException(
                    "Cannot cancel escrow in state: "
                            + escrow.getStatus()
            );
        }

        escrow.setStatus("CANCELLED");
        escrow.setUpdatedAt(LocalDateTime.now());

        EscrowTransaction saved = repository.save(escrow);

        auditService.logAction(
                getCurrentAdmin(),
                "FORCE_CANCEL",
                escrowId,
                "Admin force cancelled escrow"
        );

        return saved;
    }

    // ===============================
    // ADMIN FORCE REFUND
    // ===============================
    @Override
    public EscrowTransaction adminForceRefund(Long escrowId) {

        EscrowTransaction escrow = repository.findById(escrowId)
                .orElseThrow(() ->
                        new EscrowNotFoundException("Escrow not found"));

        if (!"CANCELLED".equals(escrow.getStatus())) {
            throw new InvalidEscrowStateException(
                    "Refund allowed only for CANCELLED escrow. Current state: "
                            + escrow.getStatus()
            );
        }

        escrow.setStatus("REFUNDED");
        escrow.setCompletedAt(LocalDateTime.now());
        escrow.setUpdatedAt(LocalDateTime.now());

        EscrowTransaction saved = repository.save(escrow);

        auditService.logAction(
                getCurrentAdmin(),
                "FORCE_REFUND",
                escrowId,
                "Admin force refunded escrow"
        );

        return saved;
    }

    // ===============================
    // ADMIN VIEW ALL ESCROWS
    // ===============================
    @Override
    public List<EscrowTransaction> getAllEscrows() {
        return repository.findAll();
    }

    // ===============================
    // HELPER: CURRENT ADMIN EMAIL
    // ===============================
    private String getCurrentAdmin() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }
}
