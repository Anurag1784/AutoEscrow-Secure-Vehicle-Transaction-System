package com.autoescrow.escrow.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autoescrow.escrow.dto.*;
import com.autoescrow.escrow.service.AdminReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/admin/reports")
@PreAuthorize("hasRole('ADMIN')")
@Tag(
    name = "Admin – Reports",
    description = "Analytical reports for escrow and wallet data"
)
@SecurityRequirement(name = "BearerAuth")
public class AdminReportController {

    private final AdminReportService reportService;

    public AdminReportController(AdminReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/escrow-summary")
    @Operation(summary = "Escrow Summary Report")
    public List<EscrowSummaryReportDTO> getEscrowSummary() {
        return reportService.getEscrowSummary();
    }

    @GetMapping("/money-flow")
    @Operation(summary = "Money Flow Report")
    public MoneyFlowReportDTO getMoneyFlowReport() {
        return reportService.getMoneyFlowReport();
    }

    @GetMapping("/seller-earnings")
    @Operation(summary = "Seller Earnings Report")
    public List<SellerEarningsDTO> getSellerEarningsReport() {
        return reportService.getSellerEarningsReport();
    }

    @GetMapping("/daily-trend")
    @Operation(summary = "Daily Escrow Trend Report")
    public List<DailyEscrowTrendDTO> getDailyEscrowTrend() {
        return reportService.getDailyEscrowTrend();
    }
}
