package org.hackathon.buss.service;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.model.Route;
import org.hackathon.buss.model.stats.RouteStatsByDay;
import org.hackathon.buss.repository.RouteStatsByDayRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RouteStatsByDayService {

    private final RouteStatsByDayRepository repository;

    public Optional<RouteStatsByDay> findByRoute(Route route, Long id) {
        return repository.findRouteStatsByRouteAndRouteStatsByDayId(route, id);
    }
}
