package com.autoescrow.escrow.history.service;

import java.util.List;
import com.autoescrow.escrow.history.entity.EscrowHistory;

public interface EscrowHistoryService {

    void recordEvent(Long escrowId, String action, String performedBy);

    // 🔥 NEW – For Admin history view
    List<EscrowHistory> getHistoryByEscrowId(Long escrowId);
}
