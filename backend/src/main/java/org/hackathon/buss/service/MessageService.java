package org.hackathon.buss.service;

import lombok.AllArgsConstructor;
import org.hackathon.buss.model.Message;
import org.hackathon.buss.repository.MessageRepository;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public Message save(Message message){
        return messageRepository.save(message);
    }
}
