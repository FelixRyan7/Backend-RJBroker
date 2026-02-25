package com.broker.wallet_service.events;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

public class WalletEvent implements Serializable {

    private Long userId;
    private String type; // deposit, withdrawal, buy, sell, dividend, etc.
    private String details; // descripción breve
    private BigDecimal amount; // cantidad afectando al networth
    private Instant timestamp;

    public WalletEvent() {
    }

    public WalletEvent(Long userId, String type, String details, BigDecimal amount, Instant timestamp) {
        this.userId = userId;
        this.type = type;
        this.details = details;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
