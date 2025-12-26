package com.autoescrow.escrow.payment.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.autoescrow.escrow.payment.entity.PaymentRecord;
import com.autoescrow.escrow.payment.enums.PaymentType;
import com.autoescrow.escrow.payment.repository.PaymentRecordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentRecordServiceImpl implements PaymentRecordService {

    private final PaymentRecordRepository paymentRecordRepository;

    @Override
    public void recordPayment(
            Long escrowId,
            String fromEmail,
            String toEmail,
            BigDecimal amount,
            PaymentType paymentType) {

        PaymentRecord record = new PaymentRecord();
        record.setEscrowId(escrowId);
        record.setFromEmail(fromEmail);
        record.setToEmail(toEmail);
        record.setAmount(amount);
        record.setPaymentType(paymentType);

        paymentRecordRepository.save(record);
    }
}
