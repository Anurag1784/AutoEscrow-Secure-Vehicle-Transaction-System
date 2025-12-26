package com.autoescrow.escrow.payment.service;

import java.math.BigDecimal;

import com.autoescrow.escrow.payment.enums.PaymentType;

public interface PaymentRecordService {

    void recordPayment(
            Long escrowId,
            String fromEmail,
            String toEmail,
            BigDecimal amount,
            PaymentType paymentType
    );
}
