package org.hackathon.buss.controller;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.model.Incident;
import org.hackathon.buss.service.IncidentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/incident")
public class IncidentController {

    private final IncidentService incidentService;

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createIncident(@RequestBody Incident incident){
        incidentService.create(incident);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
