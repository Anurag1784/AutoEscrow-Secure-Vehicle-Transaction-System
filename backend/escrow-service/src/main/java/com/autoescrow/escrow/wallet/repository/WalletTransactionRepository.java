package com.autoescrow.escrow.wallet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autoescrow.escrow.wallet.entity.WalletTransaction;

public interface WalletTransactionRepository
        extends JpaRepository<WalletTransaction, Long> {

    // ==================================================
    // 1️⃣ User Ledger – Bank-style statement
    // Latest transactions first
    // ==================================================
    List<WalletTransaction> findByUserEmailOrderByCreatedAtDesc(
            String userEmail
    );

    // ==================================================
    // 2️⃣ Escrow-specific Ledger
    // All money movements linked to an escrow
    // ==================================================
    List<WalletTransaction> findByEscrowIdOrderByCreatedAtAsc(
            Long escrowId
    );

    // ==================================================
    // 3️⃣ Admin – Global Ledger (latest first)
    // ==================================================
    List<WalletTransaction> findAllByOrderByCreatedAtDesc();

    // ==================================================
    // 4️⃣ Admin – Specific User Ledger
    // (used in STEP 6 Admin API)
    // ==================================================
    List<WalletTransaction> findByUserEmail(
            String userEmail
    );
}
