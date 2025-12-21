package com.autoescrow.escrow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.autoescrow.escrow.service.EscrowService;

@RestController
@RequestMapping("/escrow")
public class EscrowController {

    @Autowired
    private EscrowService escrowService;

    // Buyer creates escrow
    @PostMapping("/create")
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

    // Seller confirms vehicle handover
    @PostMapping("/{id}/seller-confirm")
    public ResponseEntity<?> sellerConfirm(
            @PathVariable Long id,
            Authentication authentication) {

        return ResponseEntity.ok(
                escrowService.sellerConfirm(id, authentication.getName())
        );
    }

    // Buyer confirms vehicle received
    @PostMapping("/{id}/buyer-confirm")
    public ResponseEntity<?> buyerConfirm(
            @PathVariable Long id,
            Authentication authentication) {

        return ResponseEntity.ok(
                escrowService.buyerConfirm(id, authentication.getName())
        );
    }

    // Get escrow status
    @GetMapping("/{id}")
    public ResponseEntity<?> getEscrow(@PathVariable Long id) {
        return ResponseEntity.ok(
                escrowService.getEscrowById(id)
        );
    }
}
