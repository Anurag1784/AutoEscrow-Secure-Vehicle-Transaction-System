package com.autoescrow.escrow.dto;

public class EscrowSummaryReportDTO {

    private String status;
    private Long count;

    public EscrowSummaryReportDTO(String status, Long count) {
        this.status = status;
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public Long getCount() {
        return count;
    }
}
