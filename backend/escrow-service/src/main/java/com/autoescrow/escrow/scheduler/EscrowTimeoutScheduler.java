package com.autoescrow.escrow.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.autoescrow.escrow.entity.EscrowTransaction;
import com.autoescrow.escrow.repository.EscrowTransactionRepository;
import com.autoescrow.escrow.state.EscrowStatus;

@Component
public class EscrowTimeoutScheduler {

    private final EscrowTransactionRepository repository;

    public EscrowTimeoutScheduler(EscrowTransactionRepository repository) {
        this.repository = repository;
    }

    // Runs every 1 minute
    @Scheduled(fixedRate = 60000)
    public void expireEscrows() {

        System.out.println(">>> EscrowTimeoutScheduler running at " + LocalDateTime.now());

        List<EscrowTransaction> expiredEscrows =
                repository.findByStatusAndSellerConfirmDeadlineBefore(
                        EscrowStatus.FUNDS_DEPOSITED,
                        LocalDateTime.now()
                );

        System.out.println(">>> Expired escrows found: " + expiredEscrows.size());

        for (EscrowTransaction escrow : expiredEscrows) {

            System.out.println(">>> Refunding escrow ID: " + escrow.getEscrowId());

            escrow.setStatus(EscrowStatus.REFUNDED);
            escrow.setCompletedAt(LocalDateTime.now());

            repository.save(escrow);
        }
    }
}
