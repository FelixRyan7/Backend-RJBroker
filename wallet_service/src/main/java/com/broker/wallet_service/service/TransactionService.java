package com.broker.wallet_service.service;

import com.broker.wallet_service.model.Transaction;
import com.broker.wallet_service.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;


    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;

    }

    /**
     * Guarda cualquier tipo de transacción
     */
    @Transactional
    public Transaction saveTransaction(Transaction transaction) {
        transaction.setTimestamp(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    /**
     * Registra un depósito
     */
    @Transactional
    public Transaction deposit(Long userId, Double amount) {
        Transaction transaction = new Transaction(
                "DEPOSIT",     // type
                null,          // assetSymbol
                null,          // quantity
                null,          // price
                amount,        // totalAmount
                LocalDateTime.now(),
                userId
        );
        return transactionRepository.save(transaction);
    }

    /**
     * Registra una retirada
     */
    @Transactional
    public Transaction withdraw(Long userId, Double amount) {
        Transaction transaction = new Transaction(
                "WITHDRAWAL",
                null,
                null,
                null,
                amount,
                LocalDateTime.now(),
                userId
        );
        return transactionRepository.save(transaction);
    }

    /**
     * Compra de un activo
     */
    @Transactional
    public Transaction buyAsset(Long userId, String assetSymbol, Double quantity, Double pricePerUnit, Double totalAmountInLocalCurrency) {
        Transaction transaction = new Transaction(
                "BUY",
                assetSymbol,
                quantity,
                pricePerUnit,
                totalAmountInLocalCurrency,
                LocalDateTime.now(),
                userId
        );
        return transactionRepository.save(transaction);
    }

    /**
     * Venta de un activo
     */
    @Transactional
    public Transaction sellAsset(Long userId, String assetSymbol, Double quantity, Double pricePerUnit, Double totalAmountInLocalCurrency) {
        Transaction transaction = new Transaction(
                "SELL",
                assetSymbol,
                quantity,
                pricePerUnit,
                totalAmountInLocalCurrency,
                LocalDateTime.now(),
                userId
        );
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsBetween(Long userId, LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByUserIdAndTimestampBetween(userId, start, end);
    }
}

