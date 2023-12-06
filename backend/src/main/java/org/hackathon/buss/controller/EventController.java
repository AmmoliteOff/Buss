package org.hackathon.buss.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.hackathon.buss.enums.EventType;
import org.hackathon.buss.model.Event;
import org.hackathon.buss.service.EventService;
import org.hackathon.buss.util.view.NonDetailedInformation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;


    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/register")
    public ResponseEntity<String> registerEvent(@RequestBody Event event) {
        rabbitTemplate.convertAndSend( "",  "event-registration", event);
        return ResponseEntity.ok("Ok");
    }

    @PatchMapping("/process")
    @JsonView(NonDetailedInformation.class)
    public ResponseEntity<?> processAnEvent (@RequestBody Event event) {
        Event processedEvent = eventService.findById(event.getId()).orElseThrow();
        processedEvent.setProcessed(true);
        event.setDispatcher(null);
        return ResponseEntity.ok(eventService.save(processedEvent));
    }

    @GetMapping("")
    public ResponseEntity<List<EventType>> eventTypes() {
        return ResponseEntity.ok(List.of(EventType.values()));
    }
}
