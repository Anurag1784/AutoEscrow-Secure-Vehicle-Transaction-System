package com.autoescrow.escrow.service;

import java.util.List;

import com.autoescrow.escrow.dto.*;

public interface AdminReportService {

    List<EscrowSummaryReportDTO> getEscrowSummary();

    MoneyFlowReportDTO getMoneyFlowReport();

    List<SellerEarningsDTO> getSellerEarningsReport();

    List<DailyEscrowTrendDTO> getDailyEscrowTrend();
}
