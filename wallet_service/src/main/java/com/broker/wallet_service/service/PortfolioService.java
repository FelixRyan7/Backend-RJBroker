package com.broker.wallet_service.service;

import com.broker.wallet_service.events.TradeEvent;
import com.broker.wallet_service.events.WalletEvent;
import com.broker.wallet_service.model.Portfolio;
import com.broker.wallet_service.model.Transaction;
import com.broker.wallet_service.repository.PortfolioRepository;
import com.broker.wallet_service.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class PortfolioService {


    private final PortfolioRepository portfolioRepository;

    private final WalletService walletService;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final CurrencyExchangeService currencyExchangeService;

    private final TransactionService transactionService;

    public PortfolioService(
            PortfolioRepository portfolioRepository,
            WalletService walletService,KafkaTemplate<String, Object> kafkaTemplate,
            CurrencyExchangeService currencyExchangeService,
            TransactionService transactionService
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.portfolioRepository = portfolioRepository;
        this.walletService = walletService;
        this.currencyExchangeService = currencyExchangeService;
        this.transactionService = transactionService;
    }

    @Transactional
    public Portfolio buyAsset(
            Long userId,
            Long assetId,
            String assetSymbol,
            BigDecimal price,
            BigDecimal quantity
    ) {
        Portfolio portfolio = portfolioRepository
                .findByUserIdAndAssetSymbol(userId, assetSymbol)
                .orElseGet(() -> {
                    Portfolio p = new Portfolio();
                    p.setUserId(userId);
                    p.setAssetId(assetId);
                    p.setAssetSymbol(assetSymbol);
                    p.setQuantity(BigDecimal.ZERO);
                    p.setAveragePrice(BigDecimal.ZERO);
                    return p;
                });

        // nuevo average price
        BigDecimal totalCostExisting =
                portfolio.getAveragePrice().multiply(portfolio.getQuantity());

        BigDecimal totalCostNew =
                price.multiply(quantity);

        BigDecimal newQuantity =
                portfolio.getQuantity().add(quantity);

        BigDecimal newAveragePrice =
                totalCostExisting.add(totalCostNew)
                        .divide(newQuantity, 2, BigDecimal.ROUND_HALF_DOWN);

        portfolio.setQuantity(newQuantity);
        portfolio.setAveragePrice(newAveragePrice);

        BigDecimal totalCostInLocalCurrency = currencyExchangeService.convertFromUSD(totalCostNew, walletService.getCurrencyById(userId));
        walletService.withdraw(userId, totalCostInLocalCurrency);

        Portfolio updatedPortfolio = portfolioRepository.save(portfolio);

        transactionService.buyAsset(userId, assetSymbol,quantity.doubleValue(),price.doubleValue(), totalCostInLocalCurrency.doubleValue());

        //event
        TradeEvent event = new TradeEvent();
        event.setUserId(userId);
        event.setType("BUY");
        event.setAssetSymbol(assetSymbol);
        event.setPrice(price);
        event.setQuantity(quantity);
        event.setTotalAmount(totalCostInLocalCurrency);
        event.setTimestamp(Instant.now());

        kafkaTemplate.send("trade-events", userId.toString(), event);

        return updatedPortfolio;
    }

    @Transactional
    public Portfolio sellAsset(
            Long userId,
            Long assetId,
            String assetSymbol,
            BigDecimal price,
            BigDecimal quantity
    ) {
        Portfolio portfolio = portfolioRepository
                .findByUserIdAndAssetSymbol(userId, assetSymbol)
                .orElseThrow(() ->
                        new IllegalStateException("Asset not found in portfolio")
                );

        if (portfolio.getQuantity().compareTo(quantity) < 0) {
            throw new IllegalStateException("Insufficient asset quantity");
        }

        BigDecimal totalSale = price.multiply(quantity);

        BigDecimal remainingQty =
                portfolio.getQuantity().subtract(quantity);
        BigDecimal totalSaleInLocalCurrency = currencyExchangeService.convertFromUSD(totalSale, walletService.getCurrencyById(userId));
        walletService.deposit(userId, totalSaleInLocalCurrency);

        if (remainingQty.compareTo(BigDecimal.ZERO) == 0) {
            portfolioRepository.delete(portfolio);
            // registrar transacción y evento aquí
            transactionService.sellAsset(userId, assetSymbol, quantity.doubleValue(), price.doubleValue(), totalSaleInLocalCurrency.doubleValue());

            TradeEvent event = new TradeEvent();
            event.setUserId(userId);
            event.setType("SELL");
            event.setAssetSymbol(assetSymbol);
            event.setPrice(price);
            event.setQuantity(quantity);
            event.setTotalAmount(totalSaleInLocalCurrency);
            event.setTimestamp(Instant.now());
            kafkaTemplate.send("trade-events", userId.toString(), event);

            return portfolio; // ya eliminado
        }

        portfolio.setQuantity(remainingQty);
        Portfolio updatedPortfolio = portfolioRepository.save(portfolio);


        transactionService.sellAsset(userId, assetSymbol, quantity.doubleValue(), price.doubleValue(), totalSaleInLocalCurrency.doubleValue());

        TradeEvent event = new TradeEvent();
        event.setUserId(userId);
        event.setType("SELL");
        event.setAssetSymbol(assetSymbol);
        event.setPrice(price);
        event.setQuantity(quantity);
        event.setTotalAmount(totalSaleInLocalCurrency);
        event.setTimestamp(Instant.now());
        kafkaTemplate.send("trade-events", userId.toString(), event);

        return updatedPortfolio;
    }

    public List<Portfolio> getUserPortfolio(Long userId) {
        return portfolioRepository.findAllByUserId(userId);
    }
}
