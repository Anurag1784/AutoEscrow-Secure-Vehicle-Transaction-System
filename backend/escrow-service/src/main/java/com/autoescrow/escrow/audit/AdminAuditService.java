package com.autoescrow.escrow.audit;

public interface AdminAuditService {

    void logAction(
            String adminEmail,
            String action,
            Long escrowId,
            String remarks
    );
}
