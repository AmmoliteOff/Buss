package org.hackathon.buss.service;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.dto.RouteChangeDTO;
import org.hackathon.buss.model.Route;
import org.hackathon.buss.model.RouteChange;
import org.hackathon.buss.model.Stop;
import org.hackathon.buss.repository.RouteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final RouteChangeService routeChangeService;
    public Optional<Route> findById(long id) {
        return routeRepository.findById(id);
    }

    public List<Route> findAll() {
        return routeRepository.findAll();
    }

    public Route save(Route route) {
        route.setChanges(new ArrayList<>());
        return routeRepository.save(route);
    }

    public void delete(Long id) {
        routeRepository.delete(findById(id).orElseThrow());
    }

    public Route update(Long id, RouteChangeDTO routeChangeDTO) {
        Route route = routeChangeDTO.getRoute();
        route.setId(id);
        RouteChange routeChange = new RouteChange();
        routeChange.setTime(LocalDateTime.now());
        routeChange.setReason(routeChangeDTO.getReason());
        routeChange.setRoute(route);
        route.getChanges().add(routeChange);
        return save(route);
    }

    public int getNorm(Route route, int dayOfWeek, int timeInterval){
        return 3;
    }

    public int getAverageRoadTime(int time, Route route){
        return 34;
    }

    public int getAverageStopToStopTime(int time, Route route, Stop A, Stop B){
        return 4;
    }
}
