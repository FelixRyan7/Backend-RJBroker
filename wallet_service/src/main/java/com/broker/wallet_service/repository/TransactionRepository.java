package com.broker.wallet_service.repository;

import com.broker.wallet_service.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Obtener todas las transacciones de un usuario
    List<Transaction> findByUserId(Long userId);

    // Obtener transacciones de un usuario por tipo
    List<Transaction> findByUserIdAndType(Long userId, String type);

    // Obtener transacciones de un usuario por un rango de fechas
    List<Transaction> findByUserIdAndTimestampBetween(Long userId, LocalDateTime start, LocalDateTime end);

    // Obtener transacciones de un activo específico
    List<Transaction> findByUserIdAndAssetSymbol(Long userId, String assetSymbol);
}