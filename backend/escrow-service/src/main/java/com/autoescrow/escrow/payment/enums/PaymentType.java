package com.autoescrow.escrow.payment.enums;

public enum PaymentType {

    PAYMENT_LOCKED,     // Buyer -> Escrow (funds locked)
    PAYMENT_RELEASED,   // Escrow -> Seller (completion)
    PAYMENT_REFUNDED    // Escrow -> Buyer (refund)
}
