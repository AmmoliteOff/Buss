package org.hackathon.buss.service;

import lombok.AllArgsConstructor;
import org.hackathon.buss.model.Event;
import org.hackathon.buss.repository.EventRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    public void create(Event event){
        eventRepository.save(event);
    }
}
