package com.broker.message_service.controller;

import com.broker.message_service.model.Message;
import com.broker.message_service.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Obtiene todos los mensajes de un usuario por su ID
     * GET /messages/user/{userId}
     */
    @GetMapping("/user")
    public ResponseEntity<List<Message>> getMessagesByUserId(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        List<Message> messages = messageService.getMessagesByUserId(userId);
        if (messages.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(messages);
    }
}

