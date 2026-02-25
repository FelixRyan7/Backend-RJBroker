package com.broker.wallet_service.DTOS;

import java.math.BigDecimal;

public class DepositRequest {
    private BigDecimal amount;

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}