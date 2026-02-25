package com.broker.wallet_service.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String type; // BUY, SELL, DEPOSIT, WITHDRAWAL

    @Column(nullable = true)
    private String assetSymbol; // null si DEPOSIT/WITHDRAWAL

    @Column(nullable = true)
    private Double quantity; // null si DEPOSIT/WITHDRAWAL

    @Column(nullable = true)
    private Double price; // unit price of asset; null if DEPOSIT/WITHDRAWAL

    @Column(nullable = false)
    private Double totalAmount; // amount in user currency

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Long userId; // el usuario propietario de la transacción

    public Transaction() {}

    public Transaction(String type, String assetSymbol, Double quantity, Double price, Double totalAmount, LocalDateTime timestamp, Long userId) {
        this.type = type;
        this.assetSymbol = assetSymbol;
        this.quantity = quantity;
        this.price = price;
        this.totalAmount = totalAmount;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getAssetSymbol() { return assetSymbol; }
    public void setAssetSymbol(String assetSymbol) { this.assetSymbol = assetSymbol; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}


