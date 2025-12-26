package com.autoescrow.escrow.payment.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.autoescrow.escrow.payment.enums.PaymentType;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payment_record")
@Getter
@Setter
public class PaymentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(name = "escrow_id", nullable = false)
    private Long escrowId;

    @Column(name = "from_email", nullable = false)
    private String fromEmail;

    @Column(name = "to_email", nullable = false)
    private String toEmail;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
