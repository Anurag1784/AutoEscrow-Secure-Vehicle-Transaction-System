package com.autoescrow.escrow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.autoescrow.escrow.entity.EscrowTransaction;
import com.autoescrow.escrow.state.EscrowStatus;

public interface EscrowReportRepository
        extends JpaRepository<EscrowTransaction, Long> {

    // ==========================================================
    // 1️⃣ Escrow Summary – Count by Status
    // ==========================================================
    @Query("""
        SELECT e.status, COUNT(e)
        FROM EscrowTransaction e
        GROUP BY e.status
    """)
    List<Object[]> countEscrowsByStatus();


    // ==========================================================
    // 2️⃣ Money Flow – Total escrowed amount
    // ==========================================================
    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM EscrowTransaction e
    """)
    Double totalEscrowedAmount();


    // ==========================================================
    // 3️⃣ Money Flow – Total amount by status
    // ==========================================================
    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM EscrowTransaction e
        WHERE e.status = :status
    """)
    Double totalAmountByStatus(EscrowStatus status);


    // ==========================================================
    // 4️⃣ Seller Earnings – Group by seller
    // ==========================================================
    @Query("""
        SELECT e.sellerEmail, SUM(e.amount), COUNT(e)
        FROM EscrowTransaction e
        WHERE e.status = 'COMPLETED'
        GROUP BY e.sellerEmail
    """)
    List<Object[]> sellerEarningsReport();


    // ==========================================================
    // 5️⃣ Daily Escrow Trend – Escrows created per day
    // (NULL-safe)
    // ==========================================================
    @Query("""
        SELECT DATE(e.createdAt), COUNT(e)
        FROM EscrowTransaction e
        WHERE e.createdAt IS NOT NULL
        GROUP BY DATE(e.createdAt)
        ORDER BY DATE(e.createdAt)
    """)
    List<Object[]> dailyEscrowTrend();
}
