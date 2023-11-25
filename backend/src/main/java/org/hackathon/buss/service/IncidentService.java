package org.hackathon.buss.service;

import lombok.AllArgsConstructor;
import org.hackathon.buss.model.Incident;
import org.hackathon.buss.repository.IncidentRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final NotificationService notificationService;
    public void create(Incident incident){
        incidentRepository.save(incident);
        notificationService.createIncident();
    }
}
