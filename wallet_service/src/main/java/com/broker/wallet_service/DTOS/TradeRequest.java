package com.broker.wallet_service.DTOS;

import com.broker.wallet_service.model.TradeType;

import java.math.BigDecimal;

public record TradeRequest(
        TradeType tradeType,          // BUY o SELL
        Long assetId,
        BigDecimal price,
        BigDecimal quantity,
        String assetSymbol

) {}

