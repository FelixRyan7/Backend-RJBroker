package com.broker.wallet_service.DTOS;

public record AiQuestionRequest(
        String question,
        String assetName,
        String assetSymbol,
        Double price
) {}
