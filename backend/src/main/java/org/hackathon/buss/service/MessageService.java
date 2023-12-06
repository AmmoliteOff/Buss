package org.hackathon.buss.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.hackathon.buss.model.Driver;
import org.hackathon.buss.model.Message;
import org.hackathon.buss.model.User;
import org.hackathon.buss.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;


    @Transactional
    public Message save(Message message){
        message.setSendAt(LocalDateTime.now());
        return messageRepository.save(message);
    }
}
