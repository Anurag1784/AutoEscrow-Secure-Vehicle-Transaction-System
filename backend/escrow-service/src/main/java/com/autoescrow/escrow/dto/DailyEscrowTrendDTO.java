package com.autoescrow.escrow.dto;

import java.time.LocalDate;

public class DailyEscrowTrendDTO {

    private LocalDate date;
    private Long count;

    public DailyEscrowTrendDTO(LocalDate date, Long count) {
        this.date = date;
        this.count = count;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getCount() {
        return count;
    }
}
