package org.hackathon.buss.service;


import lombok.AllArgsConstructor;
import org.hackathon.buss.model.Chat;
import org.hackathon.buss.model.Dispatcher;
import org.hackathon.buss.model.Event;
import org.hackathon.buss.model.Message;
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


    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    public void save(Event event) {
        if(event.getId() == null) {
            Dispatcher dispatcher = userService.findMinLoadedDispatcher();
            event.setDateTime(LocalDateTime.now());
            dispatcher.getEvents().add(event);
            event.setDispatcher(dispatcher);
            Chat chat = chatService.findByDriver(event.getDriver()).get();
            chat.setDispatcher(dispatcher);
            Message message = new Message();
            message.setContent(event.getType().name());
            message.setChat(chat);
            message.setSender(event.getDriver());
            message.setReceiver(dispatcher);
            message.setSendAt(LocalDateTime.now());
            chat.getMessages().add(message);
            Chat savedChat = chatService.save(chat);
            dispatcher.getChat().add(savedChat);
            event.setChat(savedChat);
        }
        eventRepository.save(event);
    }
}
