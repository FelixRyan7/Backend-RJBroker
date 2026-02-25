package com.broker.asset_service.DTO;

import java.math.BigDecimal;

public record AssetAdittionalDataDto (
        String logo,
        BigDecimal dividendAmount,
        BigDecimal marketCap
        
){}

