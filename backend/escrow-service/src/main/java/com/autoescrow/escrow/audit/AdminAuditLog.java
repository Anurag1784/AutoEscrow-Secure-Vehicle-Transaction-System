package com.autoescrow.escrow.audit;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "admin_audit_log")
public class AdminAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String adminEmail;

    private String action; // FORCE_CANCEL, FORCE_REFUND

    private Long escrowId;

    private LocalDateTime actionTime;

    private String remarks;

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAdminEmail() { return adminEmail; }
    public void setAdminEmail(String adminEmail) { this.adminEmail = adminEmail; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public Long getEscrowId() { return escrowId; }
    public void setEscrowId(Long escrowId) { this.escrowId = escrowId; }

    public LocalDateTime getActionTime() { return actionTime; }
    public void setActionTime(LocalDateTime actionTime) { this.actionTime = actionTime; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
