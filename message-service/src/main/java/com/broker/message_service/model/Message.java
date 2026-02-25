package com.broker.message_service.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // obtenido del JWT

    private String type; // deposit, withdrawal, buy, sell, dividend, etc.

    private String details; // breve descripción del evento

    private BigDecimal amount; // cantidad que afecta al networth

    private Instant timestamp; // fecha y hora del evento

    public Message(Long id, Long userId, String type, String details, BigDecimal amount, Instant timestamp) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.details = details;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public Message() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
