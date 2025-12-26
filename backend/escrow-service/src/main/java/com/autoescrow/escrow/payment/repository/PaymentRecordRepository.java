package com.autoescrow.escrow.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autoescrow.escrow.payment.entity.PaymentRecord;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {

    List<PaymentRecord> findByEscrowId(Long escrowId);

    List<PaymentRecord> findByFromEmailOrToEmail(String fromEmail, String toEmail);
}
