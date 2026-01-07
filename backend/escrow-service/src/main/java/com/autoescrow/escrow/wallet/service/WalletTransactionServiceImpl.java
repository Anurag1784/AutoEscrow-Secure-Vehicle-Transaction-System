package com.autoescrow.escrow.wallet.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.autoescrow.escrow.wallet.entity.WalletTransaction;
import com.autoescrow.escrow.wallet.enums.WalletTransactionType;
import com.autoescrow.escrow.wallet.repository.WalletTransactionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class WalletTransactionServiceImpl
        implements WalletTransactionService {

    private final WalletTransactionRepository repository;

    public WalletTransactionServiceImpl(
            WalletTransactionRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public void recordTransaction(
            String userEmail,
            BigDecimal amount,
            WalletTransactionType type,
            BigDecimal balanceAfter,
            Long escrowId,
            String description
    ) {

        WalletTransaction tx = new WalletTransaction();
        tx.setUserEmail(userEmail);
        tx.setAmount(amount);
        tx.setType(type);
        tx.setBalanceAfter(balanceAfter);
        tx.setEscrowId(escrowId);
        tx.setDescription(description);
        tx.setCreatedAt(LocalDateTime.now());

        repository.save(tx);
    }
}
