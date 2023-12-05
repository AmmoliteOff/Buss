package org.hackathon.buss.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.hackathon.buss.model.Chat;
import org.hackathon.buss.model.Dispatcher;
import org.hackathon.buss.model.Event;
import org.hackathon.buss.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final UserService userService;
    private final ChatService chatService;

    @PersistenceContext
    private final EntityManager entityManager;

    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    @Transactional
    public Event save(Event event) {
        if(event.getId() == null) {
            Dispatcher dispatcher = userService.findMinLoadedDispatcher();
            event.setDateTime(LocalDateTime.now());
            dispatcher.getEvents().add(event);
            event.setDispatcher(dispatcher);
            event.setDateTime(LocalDateTime.now());
            Chat chat = chatService.findByDriver(event.getDriver()).orElseThrow();
            chat.setDispatcher(dispatcher);
            event.setChat(chat);
        }
        return eventRepository.save(event);
    }
}
