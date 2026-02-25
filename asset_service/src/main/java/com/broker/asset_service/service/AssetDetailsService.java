package com.broker.asset_service.service;

import com.broker.asset_service.DTO.*;
import com.broker.asset_service.model.Asset;
import com.broker.asset_service.model.AssetType;
import org.springframework.stereotype.Service;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

import java.io.IOException;
import java.util.Calendar;


import java.math.BigDecimal;
import java.util.List;

@Service
public class AssetDetailsService {

    private final AssetService assetService;

    private final AssetHistoricalService assetHistoricalService;

    private final FinnhubClient finnhubClient;
    private final CoinGeckoClient coinGeckoClient;

    public AssetDetailsService(
            AssetService assetService,
            AssetHistoricalService assetHistoricalService,
            FinnhubClient finnhubClient,
            CoinGeckoClient coinGeckoClient
    ) {
        this.assetService = assetService;
        this.assetHistoricalService = assetHistoricalService;
        this.finnhubClient = finnhubClient;
        this.coinGeckoClient = coinGeckoClient;
    }

    // Metodo para obtener datos de activos no cripto
    public AssetDetailsDto getAssetDetails(Long assetId) {

        // 1️⃣ Asset desde tu DB
        Asset asset = assetService.getAssetById(assetId);


        // 2️⃣ Precio desde Finnhub
        AssetPriceDto priceDto =
                finnhubClient.getQuote(asset.getSymbol());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        AssetFinancialsDto financialsDto =
                finnhubClient.getFinancials(asset.getSymbol());



        boolean needUpdate = false;


        if (asset.getLogo() == null || asset.getLogo().isEmpty()) {
            String logo = finnhubClient.getLogo(asset.getSymbol());
            asset.setLogo(logo);
            needUpdate = true;
        }

        if (needUpdate) {
            assetService.save(asset);
        }

        // 3️⃣ Calcular variación %
        BigDecimal changePercent = calculateChangePercent(
                priceDto.price(),
                priceDto.open()
        );

        AssetHistoricalService.HistoricalOpenPrices historicalPrices = assetHistoricalService.getHistoricalOpenPrices(asset.getSymbol());

        BigDecimal open5D = historicalPrices.open5D;
        BigDecimal open1M = historicalPrices.open1M;
        BigDecimal open1Y = historicalPrices.open1Y;

        // 4️⃣ Construir DTO final
        return new AssetDetailsDto(
                asset.getId(),
                asset.getName(),
                asset.getSymbol(),
                priceDto.price(),
                priceDto.open(),
                priceDto.change(),
                changePercent,
                asset.getLogo(),
                asset.getDescription(),
                asset.getCurrency(),
                financialsDto.marketCap(),
                open5D,
                open1M,
                open1Y,
                asset.getIsin(),
                asset.getMarket(),
                financialsDto.dividendAmount(),
                financialsDto.peRatio(),
                financialsDto.eps(),
                financialsDto.ebitda(),
                financialsDto.netIncome()

        );
    }



    public CriptoDetailsDto getCryptoDetails(Long assetId) {

        // 1️⃣ Traemos asset desde DB
        Asset asset = assetService.getAssetById(assetId);

        // 2️⃣ Obtenemos datos de CoinGecko
        CryptoPriceDataDto crypto = coinGeckoClient.getCryptoData(asset.getSymbol());



        // 5️⃣ Construimos DTO final
        return new CriptoDetailsDto(
                asset.getId(),
                asset.getName(),
                asset.getSymbol(),
                crypto.price(),
                crypto.open24h(),
                crypto.change(),
                crypto.changePercent(),
                asset.getLogo(),
                asset.getDescription(),
                asset.getMarketCap(),
                crypto.open5D(),
                crypto.open1M(),
                crypto.open1Y()
        );
    }


    private BigDecimal calculateChangePercent(
            BigDecimal price,
            BigDecimal open
    ) {
        if (open == null || open.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return price
                .subtract(open)
                .divide(open, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }


}

