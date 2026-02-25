package com.broker.wallet_service.service;

import com.broker.wallet_service.DTOS.PortfolioAssetResponse;
import com.broker.wallet_service.DTOS.WalletSumaryResponse;
import com.broker.wallet_service.events.WalletEvent;
import com.broker.wallet_service.model.Portfolio;
import com.broker.wallet_service.model.Transaction;
import com.broker.wallet_service.model.Wallet;
import com.broker.wallet_service.repository.PortfolioRepository;
import com.broker.wallet_service.repository.WalletRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WalletService {
    private final WalletRepository walletRepository;

    private final PortfolioRepository portfolioRepository;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final TransactionService transactionService;


    public WalletService(
            WalletRepository walletRepository,
            PortfolioRepository portfolioRepository,
            KafkaTemplate<String, Object> kafkaTemplate,
            TransactionService transactionService

    ) {
        this.walletRepository = walletRepository;
        this.portfolioRepository = portfolioRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.transactionService = transactionService;

    }

    public WalletSumaryResponse getWalletSummary(Long userId) {

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        List<Portfolio> portfolios = portfolioRepository.findAllByUserId(userId);
        List<PortfolioAssetResponse> assets = portfolios.stream()
                .map(p -> new PortfolioAssetResponse(
                        p.getAssetId(),
                        p.getAssetSymbol(),
                        p.getQuantity(),
                        p.getAveragePrice()
                ))
                .collect(Collectors.toList());

        return new WalletSumaryResponse(
                wallet.getBalance(),
                wallet.getCurrency(),
                assets
        );

    }
    public BigDecimal depositCash(Long userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
        transactionService.deposit(userId, amount.doubleValue());


        WalletEvent event = new WalletEvent();
        event.setUserId(userId);
        event.setAmount(amount);
        event.setType("DEPOSIT"); // tipo de evento
        event.setDetails("Deposito de efectivo"); // descripción corta
        event.setTimestamp(Instant.now());

        kafkaTemplate.send("wallet-events", event.getUserId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        System.out.println(" ERROR enviando evento a Kafka");
                        ex.printStackTrace();
                    } else {
                        System.out.println(
                                "Evento enviado a Kafka | topic="
                                        + result.getRecordMetadata().topic()
                                        + " | partition=" + result.getRecordMetadata().partition()
                                        + " | offset=" + result.getRecordMetadata().offset()
                        );
                    }
                });
        return wallet.getBalance();
    }
    public WalletSumaryResponse deposit(Long userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
        List<Portfolio> portfolios = portfolioRepository.findAllByUserId(userId);
        List<PortfolioAssetResponse> assets = portfolios.stream()
                .map(p -> new PortfolioAssetResponse(
                        p.getAssetId(),
                        p.getAssetSymbol(),
                        p.getQuantity(),
                        p.getAveragePrice()
                ))
                .collect(Collectors.toList());

        return new WalletSumaryResponse(wallet.getBalance(), wallet.getCurrency(), assets);
    }

    public WalletSumaryResponse withdraw(Long userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        // Validación: saldo suficiente
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);

        List<Portfolio> portfolios = portfolioRepository.findAllByUserId(userId);
        List<PortfolioAssetResponse> assets = portfolios.stream()
                .map(p -> new PortfolioAssetResponse(
                        p.getAssetId(),
                        p.getAssetSymbol(),
                        p.getQuantity(),
                        p.getAveragePrice()
                ))
                .collect(Collectors.toList());

        return new WalletSumaryResponse(wallet.getBalance(), wallet.getCurrency(), assets);
    }

    public String getCurrencyById(Long userId){
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        String currency = wallet.getCurrency();

        return currency;
    }


}

