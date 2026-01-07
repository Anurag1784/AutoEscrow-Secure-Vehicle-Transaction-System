package com.autoescrow.escrow.dto;

import java.math.BigDecimal;

public class SellerEarningsDTO {

    private String sellerEmail;
    private BigDecimal totalAmount;
    private Long completedEscrows;

    public SellerEarningsDTO(
            String sellerEmail,
            BigDecimal totalAmount,
            Long completedEscrows
    ) {
        this.sellerEmail = sellerEmail;
        this.totalAmount = totalAmount;
        this.completedEscrows = completedEscrows;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Long getCompletedEscrows() {
        return completedEscrows;
    }
}
