package com.broker.message_service.service;

import com.broker.message_service.model.Message;
import com.broker.message_service.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService
{
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getMessagesByUserId(Long userId) {
        return messageRepository.findByUserId(userId);
    }
}
