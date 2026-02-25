package com.broker.wallet_service.DTOS;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class PortfolioReportDto {

    // 1️⃣ Información general
    private double netWorthCurrent;
    private double netWorthSnapshot;
    private double cashAdjustment;
    private double totalGainLoss;
    private double percentGainLoss;
    private LocalDate snapshotDate;
    private LocalDate reportDate;

    // 2️⃣ Movimientos de portfolio
    private List<Map<String, Object>> incorporations; // nuevos assets
    private List<Map<String, Object>> increments;     // assets aumentados
    private List<Map<String, Object>> decrements;     // assets reducidos
    private List<Map<String, Object>> removals;       // assets vendidos por completo
    // 3️⃣ Benchmark comparison
    private List<BenchmarkResult> benchmarks;    // SPY, QQQ, VGK, URTH, etc.

    // 4️⃣ Assets actuales
    private List<AssetsDataForAiDto> currentAssets;        // cantidad, precio, categoría, sector, país

    // 5️⃣ Resumen de IA
    private String aiSummary;                     // texto resumido evaluando comportamiento
    private PortfolioAiAnalysisDto aiAnalysis;   // JSON estructurado con análisis por apartado

    public PortfolioReportDto() {

    }
    public PortfolioReportDto(double netWorthCurrent, double netWorthSnapshot, double cashAdjustment, double totalGainLoss, double percentGainLoss, LocalDate snapshotDate, LocalDate reportDate, List<Map<String, Object>> incorporations, List<Map<String, Object>> increments, List<Map<String, Object>> decrements, List<Map<String, Object>> removals, List<BenchmarkResult> benchmarks, List<AssetsDataForAiDto> currentAssets, String aiSummary, PortfolioAiAnalysisDto aiAnalysis) {
        this.netWorthCurrent = netWorthCurrent;
        this.netWorthSnapshot = netWorthSnapshot;
        this.cashAdjustment = cashAdjustment;
        this.totalGainLoss = totalGainLoss;
        this.percentGainLoss = percentGainLoss;
        this.snapshotDate = snapshotDate;
        this.reportDate = reportDate;
        this.incorporations = incorporations;
        this.increments = increments;
        this.decrements = decrements;
        this.removals = removals;
        this.benchmarks = benchmarks;
        this.currentAssets = currentAssets;
        this.aiSummary = aiSummary;
        this.aiAnalysis = aiAnalysis;
    }

    public double getNetWorthCurrent() {
        return netWorthCurrent;
    }

    public void setNetWorthCurrent(double netWorthCurrent) {
        this.netWorthCurrent = netWorthCurrent;
    }

    public double getNetWorthSnapshot() {
        return netWorthSnapshot;
    }

    public void setNetWorthSnapshot(double netWorthSnapshot) {
        this.netWorthSnapshot = netWorthSnapshot;
    }

    public double getCashAdjustment() {
        return cashAdjustment;
    }

    public void setCashAdjustment(double cashAdjustment) {
        this.cashAdjustment = cashAdjustment;
    }

    public double getTotalGainLoss() {
        return totalGainLoss;
    }

    public void setTotalGainLoss(double totalGainLoss) {
        this.totalGainLoss = totalGainLoss;
    }

    public double getPercentGainLoss() {
        return percentGainLoss;
    }

    public void setPercentGainLoss(double percentGainLoss) {
        this.percentGainLoss = percentGainLoss;
    }

    public LocalDate getSnapshotDate() {
        return snapshotDate;
    }

    public void setSnapshotDate(LocalDate snapshotDate) {
        this.snapshotDate = snapshotDate;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public List<Map<String, Object>> getIncorporations() {
        return incorporations;
    }

    public void setIncorporations(List<Map<String, Object>> incorporations) {
        this.incorporations = incorporations;
    }

    public List<Map<String, Object>> getIncrements() {
        return increments;
    }

    public void setIncrements(List<Map<String, Object>> increments) {
        this.increments = increments;
    }

    public List<Map<String, Object>> getDecrements() {
        return decrements;
    }

    public void setDecrements(List<Map<String, Object>> decrements) {
        this.decrements = decrements;
    }

    public List<Map<String, Object>> getRemovals() {
        return removals;
    }

    public void setRemovals(List<Map<String, Object>> removals) {
        this.removals = removals;
    }

    public List<BenchmarkResult> getBenchmarks() {
        return benchmarks;
    }

    public void setBenchmarks(List<BenchmarkResult> benchmarks) {
        this.benchmarks = benchmarks;
    }

    public List<AssetsDataForAiDto> getCurrentAssets() {
        return currentAssets;
    }

    public void setCurrentAssets(List<AssetsDataForAiDto> currentAssets) {
        this.currentAssets = currentAssets;
    }

    public String getAiSummary() {
        return aiSummary;
    }

    public void setAiSummary(String aiSummary) {
        this.aiSummary = aiSummary;
    }

    public PortfolioAiAnalysisDto getAiAnalysis() {
        return aiAnalysis;
    }

    public void setAiAnalysis(PortfolioAiAnalysisDto aiJsonSummary) {
        this.aiAnalysis = aiJsonSummary;
    }
}