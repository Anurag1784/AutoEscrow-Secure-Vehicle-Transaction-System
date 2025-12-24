package com.autoescrow.escrow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autoescrow.escrow.audit.AdminAuditLog;
import com.autoescrow.escrow.audit.AdminAuditLogRepository;
import com.autoescrow.escrow.entity.EscrowTransaction;
import com.autoescrow.escrow.service.EscrowService;

@RestController
@RequestMapping("/api/admin/escrows")
@PreAuthorize("hasRole('ADMIN')")
public class AdminEscrowController {

    @Autowired
    private EscrowService escrowService;

    @Autowired
    private AdminAuditLogRepository auditLogRepository;

    // ==================================================
    // 1️⃣ ADMIN: View all escrows
    // ==================================================
    @GetMapping
    public List<EscrowTransaction> getAllEscrows() {
        return escrowService.getAllEscrows();
    }

    // ==================================================
    // 2️⃣ ADMIN: View escrows by status
    // ==================================================
    @GetMapping("/status/{status}")
    public List<EscrowTransaction> getEscrowsByStatus(
            @PathVariable String status) {

        return escrowService.getAllEscrows()
                .stream()
                .filter(e ->
                        e.getStatus() != null &&
                        e.getStatus().equalsIgnoreCase(status)
                )
                .toList();
    }

    // ==================================================
    // 3️⃣ ADMIN: Force cancel escrow
    // ==================================================
    @PostMapping("/{id}/force-cancel")
    public EscrowTransaction forceCancel(@PathVariable Long id) {
        return escrowService.adminForceCancel(id);
    }

    // ==================================================
    // 4️⃣ ADMIN: Force refund escrow
    // ==================================================
    @PostMapping("/{id}/force-refund")
    public EscrowTransaction forceRefund(@PathVariable Long id) {
        return escrowService.adminForceRefund(id);
    }

    // ==================================================
    // 5️⃣ ADMIN: View audit logs
    // ==================================================
    @GetMapping("/audit-logs")
    public List<AdminAuditLog> getAuditLogs() {
        return auditLogRepository.findAll();
    }
}
