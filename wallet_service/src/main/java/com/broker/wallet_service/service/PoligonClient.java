package com.broker.wallet_service.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class PoligonClient {

    private final String apiKey = "iRzTOW3cvLbU96baGfa9dKC8cP5h__4u";

    private final RestTemplate restTemplate = new RestTemplate();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    /**
     * Devuelve el precio de apertura de un símbolo en una fecha concreta.
     * @param symbol ticker de la acción
     * @param date fecha para el precio
     * @return BigDecimal con precio de apertura o null si falla
     */

    public BigDecimal getOpenPrice(String symbol, LocalDate date) {
        try {
            // Retroceder hasta el último día hábil si es fin de semana
            while (date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                date = date.minusDays(1);
            }
            Thread.sleep(250); // 250ms entre requests

            String dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE); // YYYY-MM-DD

            String url = UriComponentsBuilder
                    .fromUriString("https://api.polygon.io/v1/open-close/{symbol}/{date}")
                    .queryParam("adjusted", "true")
                    .queryParam("apiKey", apiKey)
                    .buildAndExpand(Map.of(
                            "symbol", symbol,
                            "date", dateStr
                    ))
                    .toUriString();

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("open")) {
                Number open = (Number) response.get("open");
                return new BigDecimal(open.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
