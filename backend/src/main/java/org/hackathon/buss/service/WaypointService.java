package org.hackathon.buss.service;

import lombok.AllArgsConstructor;
import org.hackathon.buss.model.Waypoint;
import org.hackathon.buss.repository.WaypointRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class WaypointService {
    private final WaypointRepository waypointRepository;

    public Set<Waypoint> getAll() {
        return Set.copyOf(waypointRepository.findAll());
    }

    public Optional<Waypoint> findByLatitudeAndLongitude(double latitude, double longitude) {
        return waypointRepository.findByLatitudeAndLongitude(latitude, longitude);
    }
}
