package com.broker.wallet_service.DTOS;

public record AiTermRequest(
        String termKey,
        String value,
        String assetName,
        String assetSymbol
) {}
