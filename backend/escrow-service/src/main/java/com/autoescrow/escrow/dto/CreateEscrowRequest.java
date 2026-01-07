package com.autoescrow.escrow.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to create a new escrow")
public class CreateEscrowRequest {

    @Schema(example = "seller@test.com")
    private String sellerEmail;

    @Schema(example = "101")
    private Long vehicleId;

    @Schema(example = "850000")
    private BigDecimal amount;

    // getters & setters

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
