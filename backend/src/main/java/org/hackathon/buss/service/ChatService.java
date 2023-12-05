package org.hackathon.buss.service;

import lombok.AllArgsConstructor;
import org.hackathon.buss.model.Chat;
import org.hackathon.buss.model.Driver;
import org.hackathon.buss.repository.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    public Optional<Chat> findById(Long id) {
        return chatRepository.findById(id);
    }

    public Chat save(Chat chat) {
        return chatRepository.save(chat);
    }

    public Optional<Chat> findByDriver(Driver driver) {
        return chatRepository.findByDriver(driver);
    }

}
