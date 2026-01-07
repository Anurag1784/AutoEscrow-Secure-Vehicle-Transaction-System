package com.autoescrow.escrow.wallet.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.autoescrow.escrow.wallet.dto.WalletTransactionDTO;
import com.autoescrow.escrow.wallet.entity.WalletTransaction;
import com.autoescrow.escrow.wallet.repository.WalletTransactionRepository;

@Service
public class WalletTransactionQueryServiceImpl
        implements WalletTransactionQueryService {

    private final WalletTransactionRepository repository;

    public WalletTransactionQueryServiceImpl(
            WalletTransactionRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public List<WalletTransactionDTO> getMyTransactions(String userEmail) {

        return repository.findByUserEmailOrderByCreatedAtDesc(userEmail)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<WalletTransactionDTO> getTransactionsByUser(String userEmail) {

        return repository.findByUserEmail(userEmail)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private WalletTransactionDTO toDTO(WalletTransaction tx) {

        return new WalletTransactionDTO(
                tx.getAmount(),
                tx.getType(),
                tx.getBalanceAfter(),
                tx.getDescription(),
                tx.getEscrowId(),
                tx.getCreatedAt()
        );
    }
}
