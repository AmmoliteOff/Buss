package org.hackathon.buss.service;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.model.Route;
import org.hackathon.buss.repository.RouteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;

    public Optional<Route> findById(long id) {
        return routeRepository.findById(id);
    }

    public List<Route> findAll() {
        return routeRepository.findAll();
    }

    public Route save(Route route) {
        return routeRepository.save(route);
    }

    public void delete(Long id) {
        routeRepository.delete(findById(id).orElseThrow());
    }

    public Route update(Long id, Route newRoute) {
        newRoute.setId(id);
        return save(newRoute);
    }
}
