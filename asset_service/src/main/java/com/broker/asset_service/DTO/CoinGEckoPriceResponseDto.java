package com.broker.asset_service.DTO;

import java.util.Map;

public class CoinGEckoPriceResponseDto {

    private Map<String, PriceData> data;

    public Map<String, PriceData> getData() {
        return data;
    }

    public void setData(Map<String, PriceData> data) {
        this.data = data;
    }

    public static class PriceData {
        private Double usd;
        private Double usd_24h_change;

        public Double getUsd() {
            return usd;
        }

        public void setUsd(Double usd) {
            this.usd = usd;
        }

        public Double getUsd_24h_change() {
            return usd_24h_change;
        }

        public void setUsd_24h_change(Double usd_24h_change) {
            this.usd_24h_change = usd_24h_change;
        }
    }
}
