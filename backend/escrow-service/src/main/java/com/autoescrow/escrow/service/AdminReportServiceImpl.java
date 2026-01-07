package com.autoescrow.escrow.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.autoescrow.escrow.dto.*;
import com.autoescrow.escrow.repository.EscrowReportRepository;
import com.autoescrow.escrow.state.EscrowStatus;

@Service
public class AdminReportServiceImpl implements AdminReportService {

    private final EscrowReportRepository reportRepository;

    public AdminReportServiceImpl(EscrowReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    // ==================================================
    // 1️⃣ Escrow Summary
    // ==================================================
    @Override
    public List<EscrowSummaryReportDTO> getEscrowSummary() {

        return reportRepository.countEscrowsByStatus()
                .stream()
                .map(row ->
                        new EscrowSummaryReportDTO(
                                row[0].toString(),
                                (Long) row[1]
                        )
                )
                .collect(Collectors.toList());
    }

    // ==================================================
    // 2️⃣ Money Flow Report
    // ==================================================
    @Override
    public MoneyFlowReportDTO getMoneyFlowReport() {

        BigDecimal totalEscrowed =
                BigDecimal.valueOf(reportRepository.totalEscrowedAmount());

        BigDecimal totalReleased =
                BigDecimal.valueOf(
                        reportRepository.totalAmountByStatus(EscrowStatus.COMPLETED)
                );

        BigDecimal totalRefunded =
                BigDecimal.valueOf(
                        reportRepository.totalAmountByStatus(EscrowStatus.REFUNDED)
                );

        return new MoneyFlowReportDTO(
                totalEscrowed,
                totalReleased,
                totalRefunded
        );
    }

    // ==================================================
    // 3️⃣ Seller Earnings
    // ==================================================
    @Override
    public List<SellerEarningsDTO> getSellerEarningsReport() {

        return reportRepository.sellerEarningsReport()
                .stream()
                .map(row ->
                        new SellerEarningsDTO(
                                (String) row[0],
                                (BigDecimal) row[1],
                                (Long) row[2]
                        )
                )
                .collect(Collectors.toList());
    }

    // ==================================================
    // 4️⃣ Daily Escrow Trend
    // ==================================================
    @Override
    public List<DailyEscrowTrendDTO> getDailyEscrowTrend() {

        return reportRepository.dailyEscrowTrend()
                .stream()
                .map(row ->
                new DailyEscrowTrendDTO(
                    ((Date) row[0]).toLocalDate(),
                    (Long) row[1]
                )
            )

                .collect(Collectors.toList());
    }
}
