package com.broker.wallet_service.service;

import com.broker.wallet_service.events.UserCreatedEvent;
import com.broker.wallet_service.model.Wallet;
import com.broker.wallet_service.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class UserCreatedListenerService {

    private final WalletRepository walletRepository;

    public UserCreatedListenerService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @KafkaListener(topics = "user-created-topic", groupId = "wallet-service-group1")
    public void handleUserCreated(UserCreatedEvent event) {

        Long userId = event.getUserId();

        if (walletRepository.existsByUserId(userId)) return;

        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setCurrency(event.getDefaultCurrency());
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCreatedAt(LocalDateTime.now());

        walletRepository.save(wallet);

        System.out.println("Wallet creada para usuario: " + userId);
    }
}
