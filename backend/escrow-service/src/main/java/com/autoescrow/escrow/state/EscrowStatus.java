package com.autoescrow.escrow.state;

public enum EscrowStatus {

    FUNDS_DEPOSITED,
    SELLER_CONFIRMED,
    COMPLETED,
    CANCELLED,
    REFUNDED,

    // STEP 3 – Auto expiry
    EXPIRED
}
