package com.autoescrow.escrow.state;

public enum EscrowStatus {

    // Initial state after buyer deposits funds
    FUNDS_DEPOSITED,

    // Seller has confirmed vehicle handover
    SELLER_CONFIRMED,

    // Both buyer and seller confirmed
    COMPLETED,

    // Escrow cancelled before completion
    CANCELLED,

    // Funds refunded to buyer
    REFUNDED
}
