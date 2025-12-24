package com.autoescrow.escrow.audit;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminAuditLogRepository
        extends JpaRepository<AdminAuditLog, Long> {

    List<AdminAuditLog> findByAdminEmail(String adminEmail);
}
