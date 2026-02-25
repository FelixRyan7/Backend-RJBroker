package com.broker.asset_service.service;

import com.broker.asset_service.DTO.AssetInPortfolioDTO;
import com.broker.asset_service.DTO.AssetPriceDto;
import com.broker.asset_service.DTO.CryptoPriceDataDto;
import com.broker.asset_service.model.Asset;
import com.broker.asset_service.model.AssetType;
import com.broker.asset_service.repository.AssetRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;
    private final FinnhubClient finnhubClient;
    private final CoinGeckoClient coinGeckoClient;

    public AssetService(

            FinnhubClient finnhubClient,
            CoinGeckoClient coinGeckoClient
    ) {

        this.finnhubClient = finnhubClient;
        this.coinGeckoClient = coinGeckoClient;
    }

    public List<Asset> getAllAssets(int limit) {
        // Ejemplo usando JPA Repository
        Pageable pageable = PageRequest.of(0, limit);
        return assetRepository.findAll(pageable).getContent();
    }
    public Map<AssetType, List<Asset>> getDashboardAssets(int limit) {
        Map<AssetType, List<Asset>> result = new EnumMap<>(AssetType.class);

        for (AssetType type : AssetType.values()) {
            Pageable pageable = PageRequest.of(0, limit, Sort.by("name").ascending());
            result.put(type, assetRepository.findByType(type, pageable));
        }

        return result;
    }

    public List<Asset> getAllAssetsByType(AssetType type) {
        Pageable pageable = PageRequest.of(
                0,
                Integer.MAX_VALUE,
                Sort.by("name").ascending()
        );

        return assetRepository.findByType(type, pageable);
    }

    public Asset getAssetById(Long id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new AssetNotFoundException(id));
    }

    // Guardar o actualizar un asset
    @Transactional
    public Asset save(Asset asset) {
        return assetRepository.save(asset);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class AssetNotFoundException extends RuntimeException {

        public AssetNotFoundException(Long id) {
            super("Asset not found with id: " + id);
        }
    }

    public List<AssetInPortfolioDTO> getAssetsByIds(List<Long> ids) {
        // Ejemplo simple: buscar en la base de datos y mapear a DTO
        return assetRepository.findAllById(ids)
                .stream()
                .map(asset -> {
                        BigDecimal currentPrice = getCurrentPrice(asset.getSymbol(), asset.getType());
                        return new AssetInPortfolioDTO(
                        asset.getId(),
                        asset.getSymbol(),
                        asset.getName(),
                        asset.getLogo(),
                        asset.getType(),
                        currentPrice,
                                asset.getCategory()
                );
                })
                .toList();
    }

    public BigDecimal getAssetPrice(String assetSymbol){
        // 1 Precio desde Finnhub
        AssetPriceDto priceDto =
                finnhubClient.getQuote(assetSymbol);
        BigDecimal assetPrice = priceDto.price();
        return assetPrice;
    }

    public BigDecimal getCryptoPrice(String criptoSymbol){
        CryptoPriceDataDto crypto = coinGeckoClient.getCryptoData(criptoSymbol);
        BigDecimal criptoPrice = crypto.price();
        return criptoPrice;
    }

    public BigDecimal getCurrentPrice(String symbol, AssetType type) {
        if (type == AssetType.CRYPTO) {
            return getCryptoPrice(symbol);
        } else {
            return getAssetPrice(symbol);
        }
    }
}
