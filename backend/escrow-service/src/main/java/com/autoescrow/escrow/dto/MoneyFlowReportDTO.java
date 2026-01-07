package com.autoescrow.escrow.dto;

import java.math.BigDecimal;

public class MoneyFlowReportDTO {

    private BigDecimal totalEscrowed;
    private BigDecimal totalReleased;
    private BigDecimal totalRefunded;

    public MoneyFlowReportDTO(
            BigDecimal totalEscrowed,
            BigDecimal totalReleased,
            BigDecimal totalRefunded
    ) {
        this.totalEscrowed = totalEscrowed;
        this.totalReleased = totalReleased;
        this.totalRefunded = totalRefunded;
    }

    public BigDecimal getTotalEscrowed() {
        return totalEscrowed;
    }

    public BigDecimal getTotalReleased() {
        return totalReleased;
    }

    public BigDecimal getTotalRefunded() {
        return totalRefunded;
    }
}
