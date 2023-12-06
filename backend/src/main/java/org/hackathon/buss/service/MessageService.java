package org.hackathon.buss.service;

import lombok.AllArgsConstructor;
import org.hackathon.buss.model.Driver;
import org.hackathon.buss.model.Message;
import org.hackathon.buss.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChatService chatService;

    public Message save(Message message){
        Driver driver = new Driver(userService.findById(message.getSender().getId()).orElseThrow());
        message.setSendAt(LocalDateTime.now());
        message.setChat(chatService.findByDriver(driver).orElseThrow());
        return messageRepository.save(message);
    }
}
