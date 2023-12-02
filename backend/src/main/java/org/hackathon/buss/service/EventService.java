package org.hackathon.buss.service;

import lombok.AllArgsConstructor;
import org.hackathon.buss.model.Dispatcher;
import org.hackathon.buss.model.Event;
import org.hackathon.buss.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final UserService userService;

    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    public Event save(Event event) {
        if(event.getId() == null) {
            Dispatcher dispatcher = userService.findMinLoadedDispatcher();
            event.setDateTime(LocalDateTime.now());
            dispatcher.getEvents().add(event);
            event.setDispatcher(dispatcher);
            event.setDateTime(LocalDateTime.now());
        }
        return eventRepository.save(event);
    }
}
