package com.autoescrow.escrow.wallet.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autoescrow.escrow.wallet.entity.Wallet;
import com.autoescrow.escrow.wallet.enums.WalletTransactionType;
import com.autoescrow.escrow.wallet.exception.InsufficientBalanceException;
import com.autoescrow.escrow.wallet.exception.WalletException;
import com.autoescrow.escrow.wallet.repository.WalletRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    // 🔥 LEDGER SERVICE (NEW)
    private final WalletTransactionService walletTransactionService;

    // ===============================
    // CREATE WALLET IF NOT EXISTS
    // ===============================
    @Override
    public void createWalletIfNotExists(String userEmail) {

        if (!walletRepository.existsByUserEmail(userEmail)) {

            Wallet wallet = new Wallet();
            wallet.setUserEmail(userEmail);

            // IMPORTANT INITIALIZATION
            wallet.setAvailableBalance(BigDecimal.ZERO);
            wallet.setLockedBalance(BigDecimal.ZERO);

            walletRepository.save(wallet);
        }
    }

    // ===============================
    // CREDIT AVAILABLE BALANCE (TOP-UP)
    // ===============================
    @Override
    public void creditAvailable(String userEmail, BigDecimal amount) {

        validateAmount(amount);

        Wallet wallet = getWallet(userEmail);

        wallet.setAvailableBalance(
                wallet.getAvailableBalance().add(amount)
        );

        walletRepository.save(wallet);

        // 🔥 LEDGER ENTRY
        walletTransactionService.recordTransaction(
                userEmail,
                amount,
                WalletTransactionType.CREDIT,
                wallet.getAvailableBalance(),
                null,
                "Wallet top-up"
        );
    }

    // ===============================
    // LOCK FUNDS FOR ESCROW
    // ===============================
    @Override
    public void lockFunds(String userEmail, BigDecimal amount) {

        validateAmount(amount);

        Wallet wallet = getWallet(userEmail);

        if (wallet.getAvailableBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                "Insufficient available balance for locking funds"
            );
        }

        wallet.setAvailableBalance(
                wallet.getAvailableBalance().subtract(amount)
        );

        wallet.setLockedBalance(
                wallet.getLockedBalance().add(amount)
        );

        walletRepository.save(wallet);

        // 🔥 LEDGER ENTRY
        walletTransactionService.recordTransaction(
                userEmail,
                amount,
                WalletTransactionType.LOCK,
                wallet.getAvailableBalance(),
                null,
                "Funds locked for escrow"
        );
    }

    // ===============================
    // RELEASE LOCKED FUNDS TO SELLER
    // ===============================
    @Override
    public void releaseLockedToSeller(
            String buyerEmail,
            String sellerEmail,
            BigDecimal amount
    ) {

        validateAmount(amount);

        Wallet buyerWallet = getWallet(buyerEmail);
        Wallet sellerWallet = getWallet(sellerEmail);

        if (buyerWallet.getLockedBalance().compareTo(amount) < 0) {
            throw new WalletException(
                "Buyer does not have sufficient locked balance"
            );
        }

        buyerWallet.setLockedBalance(
                buyerWallet.getLockedBalance().subtract(amount)
        );

        sellerWallet.setAvailableBalance(
                sellerWallet.getAvailableBalance().add(amount)
        );

        walletRepository.save(buyerWallet);
        walletRepository.save(sellerWallet);

        // 🔥 LEDGER ENTRY – BUYER
        walletTransactionService.recordTransaction(
                buyerEmail,
                amount,
                WalletTransactionType.RELEASE,
                buyerWallet.getAvailableBalance(),
                null,
                "Funds released to seller"
        );

        // 🔥 LEDGER ENTRY – SELLER
        walletTransactionService.recordTransaction(
                sellerEmail,
                amount,
                WalletTransactionType.RELEASE,
                sellerWallet.getAvailableBalance(),
                null,
                "Escrow payment received"
        );
    }

    // ===============================
    // REFUND LOCKED FUNDS TO BUYER
    // ===============================
    @Override
    public void refundLockedToBuyer(String buyerEmail, BigDecimal amount) {

        validateAmount(amount);

        Wallet wallet = getWallet(buyerEmail);

        if (wallet.getLockedBalance().compareTo(amount) < 0) {
            throw new WalletException(
                "Insufficient locked balance for refund"
            );
        }

        wallet.setLockedBalance(
                wallet.getLockedBalance().subtract(amount)
        );

        wallet.setAvailableBalance(
                wallet.getAvailableBalance().add(amount)
        );

        walletRepository.save(wallet);

        // 🔥 LEDGER ENTRY
        walletTransactionService.recordTransaction(
                buyerEmail,
                amount,
                WalletTransactionType.REFUND,
                wallet.getAvailableBalance(),
                null,
                "Escrow refund"
        );
    }

    // ===============================
    // HELPER METHODS
    // ===============================
    private Wallet getWallet(String userEmail) {
        return walletRepository.findByUserEmail(userEmail)
                .orElseThrow(() ->
                        new WalletException("Wallet not found for: " + userEmail)
                );
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WalletException("Amount must be greater than zero");
        }
    }
}
