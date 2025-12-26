package com.autoescrow.escrow.state;

import com.autoescrow.escrow.exception.InvalidEscrowStateException;

public final class EscrowStateMachine {

    private EscrowStateMachine() {
        // utility class
    }

    // ===============================
    // VALID TRANSITIONS
    // ===============================

    public static void validateTransition(
            EscrowStatus current,
            EscrowStatus next
    ) {

        // Terminal states: no transition allowed
        if (current == EscrowStatus.COMPLETED ||
            current == EscrowStatus.REFUNDED) {

            throw new InvalidEscrowStateException(
                    "No transition allowed from terminal state: " + current
            );
        }

        switch (current) {

            case FUNDS_DEPOSITED -> {
                if (next != EscrowStatus.SELLER_CONFIRMED &&
                    next != EscrowStatus.REFUNDED) {

                    illegal(current, next);
                }
            }

            case SELLER_CONFIRMED -> {
                if (next != EscrowStatus.COMPLETED &&
                    next != EscrowStatus.CANCELLED) {

                    illegal(current, next);
                }
            }

            case CANCELLED -> {
                if (next != EscrowStatus.REFUNDED) {
                    illegal(current, next);
                }
            }

            default -> illegal(current, next);
        }
    }

    private static void illegal(
            EscrowStatus current,
            EscrowStatus next
    ) {
        throw new InvalidEscrowStateException(
                "Illegal escrow state transition: "
                        + current + " → " + next
        );
    }
}
