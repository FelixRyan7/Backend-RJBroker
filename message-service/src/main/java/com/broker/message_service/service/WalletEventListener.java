package com.broker.message_service.service;

import com.broker.message_service.event.WalletEvent;
import com.broker.message_service.model.Message;
import com.broker.message_service.repository.MessageRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class WalletEventListener {

    private final MessageRepository messageRepository;

    public WalletEventListener (MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @KafkaListener(topics = "wallet-events", groupId = "message-service-group", containerFactory = "walletKafkaListenerFactory")
    public void consumeWalletEvent(WalletEvent event) {
        System.out.println("Evento recibido: " + event);


        // Aquí podrías guardar el mensaje en la BBDD
        // Ejemplo:
        Message message = new Message();
        message.setUserId(event.getUserId());
        message.setType(event.getType());
        message.setAmount(event.getAmount());
        message.setDetails(event.getDetails());
        message.setTimestamp(event.getTimestamp());

        messageRepository.save(message);
    }
}

