package com.broker.wallet_service.DTOS;

import java.math.BigDecimal;

public record AssetPriceDto(
        String symbol,
        BigDecimal price,
        BigDecimal open,
        BigDecimal previousClose,
        BigDecimal change,
        String changePercent
) {}