package org.hackathon.buss.controller;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.model.Event;
import org.hackathon.buss.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/incident")
public class EventController {

    private final EventService eventService;

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createEvent(@RequestBody Event event) {
        eventService.create(event);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
