package com.autoescrow.escrow.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autoescrow.escrow.entity.EscrowTransaction;
import com.autoescrow.escrow.state.EscrowStatus;

public interface EscrowTransactionRepository
        extends JpaRepository<EscrowTransaction, Long> {

    // ==========================================================
    // STEP 1 – Escrow Expiry Scheduler
    // Finds escrows where:
    // 1) Status is FUNDS_DEPOSITED
    // 2) Seller confirmation deadline has passed
    // ==========================================================
    List<EscrowTransaction> findByStatusAndSellerConfirmDeadlineBefore(
            EscrowStatus status,
            LocalDateTime currentTime
    );

    // ==========================================================
    // Buyer-initiated cancel escrow (security-safe)
    // Ensures buyer can cancel ONLY their own escrow
    // ==========================================================
    Optional<EscrowTransaction> findByEscrowIdAndBuyerEmail(
            Long escrowId,
            String buyerEmail
    );
}
