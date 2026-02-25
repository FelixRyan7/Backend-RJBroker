package com.broker.wallet_service.events;

import java.io.Serializable;

public class UserCreatedEvent implements Serializable {

    private Long userId;
    private String email;
    private String defaultCurrency;

    public UserCreatedEvent() {
    }

    public UserCreatedEvent(Long userId, String email, String defaultCurrency) {
        this.userId = userId;
        this.email = email;
        this.defaultCurrency = defaultCurrency;
    }

    // Getters y Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

}

