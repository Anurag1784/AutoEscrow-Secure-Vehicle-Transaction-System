package com.autoescrow.escrow.wallet.controller;

import com.autoescrow.escrow.security.ServiceSecurityUtil;
import com.autoescrow.escrow.wallet.dto.WalletTopUpRequest;
import com.autoescrow.escrow.wallet.service.WalletService;
import com.autoescrow.escrow.wallet.service.WalletTransactionQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
@Tag(
    name = "Wallet",
    description = "Wallet operations such as top-up and transaction ledger"
)
@SecurityRequirement(name = "BearerAuth")
public class WalletController {

    private final WalletService walletService;
    private final WalletTransactionQueryService walletTransactionQueryService;

    public WalletController(
            WalletService walletService,
            WalletTransactionQueryService walletTransactionQueryService
    ) {
        this.walletService = walletService;
        this.walletTransactionQueryService = walletTransactionQueryService;
    }

    // ===============================
    // BUYER: WALLET TOP-UP
    // ===============================
    @PostMapping("/top-up")
    @Operation(
        summary = "Wallet Top-Up (Buyer)",
        description = "Allows a buyer to add money to their wallet"
    )
    public ResponseEntity<?> topUp(@RequestBody WalletTopUpRequest request) {

        ServiceSecurityUtil.requireRole("BUYER");

        String userEmail = ServiceSecurityUtil.getCurrentUser();

        walletService.createWalletIfNotExists(userEmail);
        walletService.creditAvailable(userEmail, request.getAmount());

        return ResponseEntity.ok("Wallet topped up successfully");
    }

    // ===============================
    // BUYER / SELLER: VIEW OWN LEDGER
    // ===============================
    @GetMapping("/transactions")
    @Operation(
        summary = "View Wallet Transactions",
        description = "Returns wallet transaction ledger for the logged-in buyer or seller"
    )
    public ResponseEntity<?> myWalletTransactions() {

        // ✅ SAFE ROLE CHECK (Buyer OR Seller)
        try {
            ServiceSecurityUtil.requireRole("BUYER");
        } catch (Exception ex) {
            ServiceSecurityUtil.requireRole("SELLER");
        }

        String userEmail = ServiceSecurityUtil.getCurrentUser();

        return ResponseEntity.ok(
                walletTransactionQueryService.getMyTransactions(userEmail)
        );
    }
}
