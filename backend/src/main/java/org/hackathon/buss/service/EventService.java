package org.hackathon.buss.service;

import lombok.AllArgsConstructor;
import org.hackathon.buss.model.Event;
import org.hackathon.buss.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    public Event save(Event event) {
        if(event.getId() == null) {
            event.setDateTime(LocalDateTime.now());
        }
        Event event1 = eventRepository.save(event);
        System.out.println(event1.getDispatcher().getEvents());
        return event1;
    }
}
