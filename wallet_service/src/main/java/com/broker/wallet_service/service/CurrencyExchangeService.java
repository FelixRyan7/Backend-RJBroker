package com.broker.wallet_service.service;



import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.util.Map;

@Service
public class CurrencyExchangeService {

    // Tasas simuladas (USD -> moneda)
    private final Map<String, BigDecimal> exchangeRates = Map.of(
            "EUR", new BigDecimal("0.84"),
            "GBP", new BigDecimal("0.73"),
            "MXN", new BigDecimal("17.10"),
            "ARS", new BigDecimal("850.00"),
            "COP", new BigDecimal("3900.00")
    );

    /**
     * Convierte un precio en USD a la moneda indicada
     *
     * @param amountUSD Precio en dólares
     * @param targetCurrency Moneda destino (EUR, MXN, ARS...)
     * @return Precio convertido
     */
    public BigDecimal convertFromUSD(BigDecimal amountUSD, String targetCurrency) {

        if (amountUSD == null || targetCurrency == null) {
            throw new IllegalArgumentException("Amount and currency must not be null");
        }

        BigDecimal rate = exchangeRates.get(targetCurrency.toUpperCase());

        if (rate == null) {
            throw new IllegalArgumentException("Unsupported currency: " + targetCurrency);
        }

        return amountUSD
                .multiply(rate)
                .setScale(2, BigDecimal.ROUND_UP);
    }
}
