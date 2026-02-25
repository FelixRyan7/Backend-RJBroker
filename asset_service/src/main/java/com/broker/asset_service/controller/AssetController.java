package com.broker.asset_service.controller;

import com.broker.asset_service.DTO.AssetDetailsDto;
import com.broker.asset_service.DTO.AssetInPortfolioDTO;
import com.broker.asset_service.DTO.AssetsIdsDto;
import com.broker.asset_service.model.Asset;
import com.broker.asset_service.model.AssetType;
import com.broker.asset_service.service.AlphaVantageClient;
import com.broker.asset_service.service.AssetDetailsService;
import com.broker.asset_service.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    @Autowired
    private AssetService assetService;
    @Autowired
    private AssetDetailsService assetDetailsService;
    private final AlphaVantageClient alphaVantageClient;

    public AssetController(AlphaVantageClient alphaVantageClient) {
        this.alphaVantageClient = alphaVantageClient;
    }

    @GetMapping("/all")
    public List<Asset> getAllAssets(
            @RequestParam(defaultValue = "1000") int limit // opcional, para no traer millones
    ) {
        return assetService.getAllAssets(limit);
    }

    @GetMapping("/getAssetsByType")
    public Map<AssetType, List<Asset>> getDashboardAssets(
            @RequestParam(defaultValue = "3") int limit
    ) {
        return assetService.getDashboardAssets(limit);
    }

    @GetMapping("/type/{type}")
    public List<Asset> getAssetsByType(@PathVariable AssetType type) {
        return assetService.getAllAssetsByType(type);
    }

    @GetMapping("/{id}")
    public Object getAssetDetails(@PathVariable Long id) {
        Asset asset = assetService.getAssetById(id);

        // 2️⃣ Comprobamos tipo y llamamos al servicio correspondiente
        return switch (asset.getType()) {
            case CRYPTO -> assetDetailsService.getCryptoDetails(asset.getId());
            case STOCK, ETF, REIT, COMMODITY -> assetDetailsService.getAssetDetails(asset.getId());
        };
    }

    @PostMapping("/Portfolio")
    public ResponseEntity<List<AssetInPortfolioDTO>> getAssetsPortfolio(@RequestBody AssetsIdsDto request) {
        List<Long> ids = request.getIds();

        // Llamas a tu servicio para obtener los assets
        List<AssetInPortfolioDTO> assets = assetService.getAssetsByIds(ids);

        return ResponseEntity.ok(assets);
    }


    /**
     * Lista assets por tipo (con paginación)

    @GetMapping
    public List<Asset> getAssetsByType(
            @RequestParam AssetType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        return assetService.getAssetsByType(type, page, size);
    }
     */
}
