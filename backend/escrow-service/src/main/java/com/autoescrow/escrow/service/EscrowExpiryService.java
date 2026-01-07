package com.autoescrow.escrow.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autoescrow.escrow.entity.EscrowTransaction;
import com.autoescrow.escrow.history.service.EscrowHistoryService;
import com.autoescrow.escrow.repository.EscrowTransactionRepository;
import com.autoescrow.escrow.state.EscrowStatus;
import com.autoescrow.escrow.wallet.service.WalletService;

@Service
public class EscrowExpiryService {

    private final EscrowTransactionRepository escrowRepository;
    private final WalletService walletService;
    private final EscrowHistoryService historyService;

    public EscrowExpiryService(
            EscrowTransactionRepository escrowRepository,
            WalletService walletService,
            EscrowHistoryService historyService
    ) {
        this.escrowRepository = escrowRepository;
        this.walletService = walletService;
        this.historyService = historyService;
    }

    /**
     * STEP 3 – Atomic auto-expiry execution
     */
    @Transactional
    public void expireEscrow(EscrowTransaction escrow) {

        // 1️⃣ Refund buyer locked amount
        walletService.refundLockedToBuyer(
                escrow.getBuyerEmail(),
                escrow.getAmount()
        );

        // 2️⃣ Update escrow state
        escrow.setStatus(EscrowStatus.EXPIRED);
        escrow.setCompletedAt(LocalDateTime.now());

        escrowRepository.save(escrow);

        // 3️⃣ History log
        historyService.recordEvent(
                escrow.getEscrowId(),
                "SYSTEM_EXPIRED",
                "SYSTEM"
        );
    }
}
