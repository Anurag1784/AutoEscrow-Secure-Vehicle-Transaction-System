package com.autoescrow.escrow.audit;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminAuditServiceImpl implements AdminAuditService {

    @Autowired
    private AdminAuditLogRepository repository;

    @Override
    public void logAction(
            String adminEmail,
            String action,
            Long escrowId,
            String remarks) {

        AdminAuditLog log = new AdminAuditLog();
        log.setAdminEmail(adminEmail);
        log.setAction(action);
        log.setEscrowId(escrowId);
        log.setRemarks(remarks);
        log.setActionTime(LocalDateTime.now());

        repository.save(log);
    }
}
