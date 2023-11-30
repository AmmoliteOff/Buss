package org.hackathon.buss.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.hackathon.buss.enums.Role;
import org.hackathon.buss.model.Dispatcher;
import org.hackathon.buss.model.Event;
import org.hackathon.buss.service.EventService;
import org.hackathon.buss.service.UserService;
import org.hackathon.buss.util.view.NonDetailedInformation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    private final UserService userService;

    @PostMapping("/report")
    @JsonView({NonDetailedInformation.class})
    public ResponseEntity<?> reportAnEvent (@RequestBody Event event) {
        if(event.getDriver().getRole().equals(Role.DRIVER)) {
           Dispatcher dispatcher = userService.findMinLoadedDispatcher();
            event.setDateTime(LocalDateTime.now());
            dispatcher.getEvents().add(event);
            event.setDispatcher(dispatcher);
            return ResponseEntity.ok(eventService.save(event));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have enough rights");
        }
    }

    @PatchMapping("/process")
    public ResponseEntity<?> processAnEvent (@RequestBody Event event) {
        Event processedEvent = eventService.findById(event.getId()).orElseThrow();
        processedEvent.setProcessed(true);
        return ResponseEntity.ok(eventService.save(processedEvent));
    }
}
