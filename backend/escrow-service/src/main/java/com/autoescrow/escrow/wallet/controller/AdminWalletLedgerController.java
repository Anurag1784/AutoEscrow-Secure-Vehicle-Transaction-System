package com.autoescrow.escrow.wallet.controller;

import com.autoescrow.escrow.security.ServiceSecurityUtil;
import com.autoescrow.escrow.wallet.service.WalletTransactionQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/wallet")
@Tag(
    name = "Admin – Wallet Ledger",
    description = "Admin APIs to view wallet transaction ledger of any user"
)
@SecurityRequirement(name = "BearerAuth")
public class AdminWalletLedgerController {

    private final WalletTransactionQueryService queryService;

    public AdminWalletLedgerController(
            WalletTransactionQueryService queryService
    ) {
        this.queryService = queryService;
    }

    // ==================================================
    // ADMIN: View wallet ledger of a specific user
    // ==================================================
    @Operation(
        summary = "View User Wallet Ledger (Admin)",
        description = "Admin can view complete wallet transaction history of any user using email"
    )
    @GetMapping("/transactions")
    public ResponseEntity<?> getUserLedger(
            @RequestParam String userEmail
    ) {
        // 🔐 HARD ADMIN CHECK
        ServiceSecurityUtil.requireRole("ADMIN");

        return ResponseEntity.ok(
                queryService.getTransactionsByUser(userEmail)
        );
    }
}
