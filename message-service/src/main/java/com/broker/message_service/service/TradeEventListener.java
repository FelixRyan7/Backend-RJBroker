package com.broker.message_service.service;

import com.broker.message_service.event.TradeEvent;
import com.broker.message_service.model.Message;
import com.broker.message_service.repository.MessageRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TradeEventListener {

    private final MessageRepository messageRepository;

    public TradeEventListener (MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @KafkaListener(topics = "trade-events", groupId = "message-service-group", containerFactory = "tradeKafkaListenerFactory")
    public void consumeTradeEvent(TradeEvent event) {
        System.out.println("Evento recibido: " + event);

        Message message = new Message();
        message.setUserId(event.getUserId());
        message.setType(event.getType());
        message.setAmount(event.getTotalAmount());
        message.setDetails(
                event.getType() + " | " +
                event.getAssetSymbol() + " | " + event.getPrice() + " | " + event.getQuantity()
        );
        message.setTimestamp(event.getTimestamp());
        messageRepository.save(message);
    }


}
