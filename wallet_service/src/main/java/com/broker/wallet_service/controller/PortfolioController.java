package com.broker.wallet_service.controller;

import com.broker.wallet_service.DTOS.*;
import com.broker.wallet_service.model.Portfolio;
import com.broker.wallet_service.model.PortfolioSnapshot;
import com.broker.wallet_service.model.Transaction;
import com.broker.wallet_service.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;
    @Autowired
    private PortfolioSnapshotService snapshotService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private BenchMarkService benchMarkService;

    @Autowired
    private PortfolioAiService portfolioAiService;

    @Autowired
    private CurrencyExchangeService currencyExchangeService;

    @PostMapping("/trade")
    public ResponseEntity<Portfolio> trade(
            @RequestBody TradeRequest request,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();

        Portfolio portfolio;
        if (request.quantity() == null ||
                request.quantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        if (request.price() == null) {
            throw new IllegalArgumentException("Price required for BUY or SELL");
        }

        switch (request.tradeType()) {
            case buy -> portfolio = portfolioService.buyAsset(
                    userId,
                    request.assetId(),
                    request.assetSymbol(),
                    request.price(),
                    request.quantity()
            );
            case sell -> portfolio = portfolioService.sellAsset(
                    userId,
                    request.assetId(),
                    request.assetSymbol(),
                    request.price(),
                    request.quantity()
            );
            default -> throw new IllegalArgumentException("Invalid trade type");
        }

        return ResponseEntity.ok(portfolio);
    }

    @GetMapping("/snapshots")
    public ResponseEntity<List<PortfolioSnapshot>> getSnapshots(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        List<PortfolioSnapshot> snapshots = snapshotService.getUserSnapshots(userId);
        return ResponseEntity.ok(snapshots);
    }

    @PostMapping("/report")
    public ResponseEntity<Map<String, Object>> generateReport(
            @RequestBody GenerateReportRequest request,
            Authentication authentication) {


        Long userId = (Long) authentication.getPrincipal();
        LocalDate snapshotDate = request.getStartSnapshotDate();
        // Buscar el snapshot del usuario en esa fecha
        Optional<PortfolioSnapshot> snapshotOpt = snapshotService.getSnapshotByDate(userId, snapshotDate);

        if (snapshotOpt.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "No se encontró snapshot para la fecha indicada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        PortfolioSnapshot snapshot = snapshotOpt.get();
        LocalDateTime start = snapshotDate.atStartOfDay(); // 2026-01-01T00:00:00
        LocalDateTime end = LocalDateTime.now();
        List<Transaction> transactionList = transactionService.getTransactionsBetween(userId, start, end);


        Map<String, AssetChange> assetQuantities = new HashMap<>();
        double cashAdjustment = 0.0;

        for (Transaction t : transactionList) {

            String symbol = t.getAssetSymbol();
            AssetChange change = assetQuantities.getOrDefault(symbol, new AssetChange());

            switch (t.getType()) {
                case "DEPOSIT":
                    cashAdjustment += t.getTotalAmount();
                    break;

                case "WITHDRAWAL":
                    cashAdjustment -= t.getTotalAmount();
                    break;

                case "BUY":
                    change.addQuantity(t.getQuantity().intValue());
                    change.addAmount(t.getTotalAmount());
                    break;

                case "SELL":
                    change.addQuantity(-t.getQuantity().intValue());
                    change.addAmount(-t.getTotalAmount()); // importante signo
                    break;
            }

            assetQuantities.put(symbol, change);
        }
        double historicalCash = snapshot.getCash() + cashAdjustment;

        List<Map<String, Object>> incorporations = new ArrayList<>();
        List<Map<String, Object>> removals = new ArrayList<>();
        List<Map<String, Object>> increments = new ArrayList<>();
        List<Map<String, Object>> decrements = new ArrayList<>();

        for (FullAssetData asset : request.getAssets()) {

            String symbol = asset.getSymbol();
            AssetChange change = assetQuantities.get(symbol);
            if (change == null) continue;

            int netChange = change.getQuantity();
            double totalAmount = change.getTotalAmount();
            double currentQty = asset.getQuantity();
            String category = asset.getCategory();

            if (netChange == currentQty) {
                incorporations.add(Map.of(
                        "symbol", symbol,
                        "quantity", currentQty,
                        "totalAmount", totalAmount,
                        "category", category
                ));
            } else if (netChange > 0) {
                increments.add(Map.of(
                        "symbol", symbol,
                        "quantity", netChange,
                        "totalAmount", totalAmount,
                        "category", category
                ));
            } else if (netChange < 0) {
                decrements.add(Map.of(
                        "symbol", symbol,
                        "quantity", Math.abs(netChange),
                        "totalAmount", totalAmount,
                        "category", category
                ));
            }
        }

        // 🔹 Detectar removals (existía movimiento pero ya no existe asset)
        Set<String> currentSymbols = request.getAssets().stream()
                .map(FullAssetData::getSymbol)
                .collect(Collectors.toSet());

        for (Map.Entry<String, AssetChange> entry : assetQuantities.entrySet()) {

            String symbol = entry.getKey();
            AssetChange change = entry.getValue();

            int netChange = change.getQuantity();
            double totalAmount = change.getTotalAmount();

            if (!currentSymbols.contains(symbol) && netChange < 0) {
                removals.add(Map.of(
                        "symbol", symbol,
                        "quantity", Math.abs(netChange),
                        "totalAmount", totalAmount
                ));
            }
        }

        List<BenchmarkResult> benchmarks = benchMarkService.getBenchmarks(snapshotDate);

        PortfolioReportDto report = new PortfolioReportDto();

// 1️⃣ Información general
        report.setNetWorthCurrent(request.getNetWorth());
        report.setNetWorthSnapshot(snapshot.getTotalValue());
        report.setCashAdjustment(cashAdjustment);
        double totalGainLoss = request.getNetWorth() - (snapshot.getTotalValue() + cashAdjustment);
        report.setTotalGainLoss(totalGainLoss);
        report.setPercentGainLoss(totalGainLoss / (snapshot.getTotalValue() + cashAdjustment) * 100);
        report.setSnapshotDate(snapshotDate);
        report.setReportDate(LocalDate.now());

// 2️⃣ Movimientos de portfolio
        report.setIncorporations(incorporations);
        report.setIncrements(increments);
        report.setDecrements(decrements);
        report.setRemovals(removals);

// 3️⃣ Benchmarks
        report.setBenchmarks(benchmarks);

// 4️⃣ Assets actuales
        // Suponiendo que report.getNetWorthCurrent() ya está calculado
        double netWorth = report.getNetWorthCurrent();

        // Lista donde guardaremos los assets transformados
        List<AssetsDataForAiDto> assetsForAi = new ArrayList<>();

        for (FullAssetData asset : request.getAssets()) {
            AssetsDataForAiDto dto = new AssetsDataForAiDto();
            dto.setSymbol(asset.getSymbol());
            dto.setName(asset.getName());
            dto.setCategory(asset.getCategory());

            // Valor total de cada asset
            double valueUSD = asset.getCurrentPrice() * asset.getQuantity();
            BigDecimal valueEUR = currencyExchangeService.convertFromUSD(BigDecimal.valueOf(valueUSD), "EUR");
            dto.setValueEUR((valueEUR).doubleValue());

            // Weight % sobre el netWorth del portfolio
            double weightPercent = (valueEUR.doubleValue() / netWorth) * 100.0;
            dto.setWeightPercent(weightPercent);

            assetsForAi.add(dto);
        }

        report.setCurrentAssets(assetsForAi);

        Map<String,Object> aiMap =
                portfolioAiService.generatePortfolioSummary(report);

        ObjectMapper mapper = new ObjectMapper();

        PortfolioAiAnalysisDto aiAnalysis =
                mapper.convertValue(aiMap, PortfolioAiAnalysisDto.class);

        report.setAiAnalysis(aiAnalysis);

        report.getAiAnalysis().getPortfolioOverview();
        report.getAiAnalysis().getAssetAllocation();
        report.getAiAnalysis().getInvestorProfile();
        report.getAiAnalysis().getPortfolioTrendAnalysis();



        System.out.println(benchmarks);
        System.out.println("actual net worth: " + request.getNetWorth());
        System.out.println("net worth from snapshot: " + snapshot.getTotalValue());
        System.out.println(cashAdjustment);
        System.out.println("total gain or loss" + (request.getNetWorth() - (snapshot.getTotalValue() + cashAdjustment)));


        System.out.println("incorporations: " + incorporations);
        System.out.println("increments: " + increments);
        System.out.println("decrements: " + decrements);
        System.out.println("removals: " + removals);
        System.out.println("cash adjustment: " + cashAdjustment);
        System.out.println("snapshot: " + snapshotOpt);
        System.out.println("networth today: " +  request.getNetWorth());
        System.out.println("my Current assets: " + request.getAssets());
        System.out.println(assetQuantities);
        System.out.println(report);


        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("report",report);


        return ResponseEntity.ok(response);
    }


}
