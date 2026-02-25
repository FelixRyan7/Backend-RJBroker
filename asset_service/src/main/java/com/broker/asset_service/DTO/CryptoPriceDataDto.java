package com.broker.asset_service.DTO;

import java.math.BigDecimal;

public record CryptoPriceDataDto (
    BigDecimal price,
    BigDecimal open24h,
    BigDecimal change,
    BigDecimal changePercent,
    BigDecimal open5D,
    BigDecimal open1M,
    BigDecimal open1Y
) {}

