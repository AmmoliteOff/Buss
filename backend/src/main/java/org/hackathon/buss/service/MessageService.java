package org.hackathon.buss.service;

import lombok.AllArgsConstructor;
import org.hackathon.buss.model.Message;
import org.hackathon.buss.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public Message save(Message message){
        message.setSendAt(LocalDateTime.now());
        return messageRepository.save(message);
    }
}
