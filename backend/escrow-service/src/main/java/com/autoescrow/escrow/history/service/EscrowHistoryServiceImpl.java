package com.autoescrow.escrow.history.service;

import com.autoescrow.escrow.history.entity.EscrowHistory;
import com.autoescrow.escrow.history.repository.EscrowHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EscrowHistoryServiceImpl implements EscrowHistoryService {

    private final EscrowHistoryRepository repository;

    public EscrowHistoryServiceImpl(EscrowHistoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void recordEvent(Long escrowId, String action, String performedBy) {

        EscrowHistory history = new EscrowHistory();
        history.setEscrowId(escrowId);
        history.setAction(action);
        history.setPerformedBy(performedBy);
        history.setPerformedAt(LocalDateTime.now());

        repository.save(history);
    }

    // 🔥 NEW METHOD
    @Override
    public List<EscrowHistory> getHistoryByEscrowId(Long escrowId) {
        return repository.findByEscrowIdOrderByPerformedAtAsc(escrowId);
    }
}
