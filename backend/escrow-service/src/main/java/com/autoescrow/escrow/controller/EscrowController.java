package com.autoescrow.escrow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.autoescrow.escrow.service.EscrowService;
import com.autoescrow.escrow.dto.CreateEscrowRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/escrow")
@Tag(
    name = "Escrow",
    description = "Escrow lifecycle APIs for buyer and seller"
)
@SecurityRequirement(name = "BearerAuth")
public class EscrowController {

    @Autowired
    private EscrowService escrowService;

    // ==================================================
    // Buyer creates escrow
    // ==================================================
    @PostMapping("/create")
    @Operation(
        summary = "Create Escrow",
        description = "Buyer creates escrow and funds are locked in wallet"
    )
    public ResponseEntity<?> createEscrow(
            @RequestBody CreateEscrowRequest request,
            Authentication authentication) {

        String buyerEmail = authentication.getName();

        return ResponseEntity.ok(
                escrowService.createEscrow(
                        buyerEmail,
                        request.getSellerEmail(),
                        request.getVehicleId(),
                        request.getAmount()
                )
        );
    }

    // ==================================================
    // Seller confirms vehicle handover
    // ==================================================
    @PostMapping("/{id}/seller-confirm")
    @Operation(
        summary = "Seller Confirmation",
        description = "Seller confirms vehicle handover"
    )
    public ResponseEntity<?> sellerConfirm(
            @PathVariable Long id,
            Authentication authentication) {

        return ResponseEntity.ok(
                escrowService.sellerConfirm(id, authentication.getName())
        );
    }

    // ==================================================
    // Buyer confirms vehicle received
    // ==================================================
    @PostMapping("/{id}/buyer-confirm")
    @Operation(
        summary = "Buyer Confirmation",
        description = "Buyer confirms escrow completion and releases funds"
    )
    public ResponseEntity<?> buyerConfirm(
            @PathVariable Long id,
            Authentication authentication) {

        return ResponseEntity.ok(
                escrowService.buyerConfirm(id, authentication.getName())
        );
    }

    // ==================================================
    // Buyer cancels escrow
    // ==================================================
    @PostMapping("/{id}/cancel")
    @Operation(
        summary = "Cancel Escrow",
        description = "Buyer cancels escrow before completion"
    )
    public ResponseEntity<?> cancelEscrow(
            @PathVariable Long id,
            Authentication authentication) {

        return ResponseEntity.ok(
                escrowService.cancelEscrow(id, authentication.getName())
        );
    }

    // ==================================================
    // Get escrow by ID
    // ==================================================
    @GetMapping("/{id}")
    @Operation(
        summary = "Get Escrow by ID",
        description = "Retrieve escrow details by escrow ID"
    )
    public ResponseEntity<?> getEscrow(@PathVariable Long id) {
        return ResponseEntity.ok(
                escrowService.getEscrowById(id)
        );
    }
}
