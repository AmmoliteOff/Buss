package org.hackathon.buss.service;

import lombok.AllArgsConstructor;
import org.hackathon.buss.model.EventMessage;
import org.hackathon.buss.repository.EventMessageRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EventMessageService {

    private final EventMessageRepository eventMessageRepository;

    public EventMessage save(EventMessage eventMessage){
        return eventMessageRepository.save(eventMessage);
    }
}
