package com.autoescrow.escrow.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.autoescrow.escrow.entity.EscrowTransaction;
import com.autoescrow.escrow.repository.EscrowTransactionRepository;
import com.autoescrow.escrow.service.EscrowExpiryService;
import com.autoescrow.escrow.state.EscrowStatus;

@Component
public class EscrowTimeoutScheduler {

    private final EscrowTransactionRepository repository;
    private final EscrowExpiryService expiryService;

    public EscrowTimeoutScheduler(
            EscrowTransactionRepository repository,
            EscrowExpiryService expiryService
    ) {
        this.repository = repository;
        this.expiryService = expiryService;
    }

    @Scheduled(fixedRate = 60000) // every 1 minute
    public void expireEscrows() {

        List<EscrowTransaction> expiredEscrows =
                repository.findByStatusAndSellerConfirmDeadlineBefore(
                        EscrowStatus.FUNDS_DEPOSITED,
                        LocalDateTime.now()
                );

        for (EscrowTransaction escrow : expiredEscrows) {
            expiryService.expireEscrow(escrow);
        }
    }
}
