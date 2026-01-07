package com.autoescrow.escrow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autoescrow.escrow.audit.AdminAuditLog;
import com.autoescrow.escrow.audit.AdminAuditLogRepository;
import com.autoescrow.escrow.entity.EscrowTransaction;
import com.autoescrow.escrow.history.entity.EscrowHistory;
import com.autoescrow.escrow.history.service.EscrowHistoryService;
import com.autoescrow.escrow.service.EscrowService;
import com.autoescrow.escrow.state.EscrowStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin/escrows")
@PreAuthorize("hasRole('ADMIN')")
@Tag(
    name = "Admin – Escrow Management",
    description = "Admin APIs for monitoring and controlling escrows"
)
@SecurityRequirement(name = "BearerAuth")
public class AdminEscrowController {

    @Autowired
    private EscrowService escrowService;

    @Autowired
    private EscrowHistoryService historyService;

    @Autowired
    private AdminAuditLogRepository auditLogRepository;

    @GetMapping
    @Operation(summary = "View All Escrows")
    public List<EscrowTransaction> getAllEscrows() {
        return escrowService.getAllEscrows();
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "View Escrows by Status")
    public List<EscrowTransaction> getEscrowsByStatus(
            @PathVariable EscrowStatus status) {

        return escrowService.getAllEscrows()
                .stream()
                .filter(e -> e.getStatus() == status)
                .toList();
    }

    @PostMapping("/{id}/force-cancel")
    @Operation(summary = "Force Cancel Escrow")
    public EscrowTransaction forceCancel(@PathVariable Long id) {
        return escrowService.adminForceCancel(id);
    }

    @PostMapping("/{id}/force-refund")
    @Operation(summary = "Force Refund Escrow")
    public EscrowTransaction forceRefund(@PathVariable Long id) {
        return escrowService.adminForceRefund(id);
    }

    @GetMapping("/audit-logs")
    @Operation(summary = "View Admin Audit Logs")
    public List<AdminAuditLog> getAuditLogs() {
        return auditLogRepository.findAll();
    }

    @GetMapping("/{escrowId}/history")
    @Operation(summary = "View Escrow History")
    public List<EscrowHistory> getEscrowHistory(
            @PathVariable Long escrowId) {

        return historyService.getHistoryByEscrowId(escrowId);
    }
}
