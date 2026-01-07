package com.autoescrow.escrow.history.repository;

import com.autoescrow.escrow.history.entity.EscrowHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EscrowHistoryRepository
        extends JpaRepository<EscrowHistory, Long> {

    List<EscrowHistory> findByEscrowIdOrderByPerformedAtAsc(Long escrowId);
}
