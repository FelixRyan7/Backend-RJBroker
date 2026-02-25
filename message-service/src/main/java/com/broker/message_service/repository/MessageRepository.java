package com.broker.message_service.repository;

import com.broker.message_service.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Obtener todos los mensajes de un usuario por userId, ordenados por timestamp descendente
    List<Message> findByUserIdOrderByTimestampDesc(Long userId);

    // Opcional: filtrar por tipo de mensaje
    List<Message> findByUserIdAndTypeOrderByTimestampDesc(Long userId, String type);

    List<Message> findByUserId(Long userId);
}
