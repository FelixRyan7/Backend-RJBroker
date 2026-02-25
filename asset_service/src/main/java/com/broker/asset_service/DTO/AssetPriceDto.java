package com.broker.asset_service.DTO;

import java.math.BigDecimal;

public record AssetPriceDto(
        String symbol,
        BigDecimal price,
        BigDecimal open,
        BigDecimal previousClose,
        BigDecimal change,
        String changePercent
) {}
