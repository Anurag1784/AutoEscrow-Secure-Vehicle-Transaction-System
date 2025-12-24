package com.autoescrow.escrow.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autoescrow.escrow.entity.EscrowTransaction;

public interface EscrowTransactionRepository
        extends JpaRepository<EscrowTransaction, Long> {

    // Used by auto-refund scheduler
    List<EscrowTransaction> findByStatusAndSellerConfirmDeadlineBefore(
            String status,
            LocalDateTime time
    );

    // Buyer-initiated cancel escrow
    Optional<EscrowTransaction> findByEscrowIdAndBuyerEmail(
            Long escrowId,
            String buyerEmail
    );
}
