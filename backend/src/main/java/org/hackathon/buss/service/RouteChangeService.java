package org.hackathon.buss.service;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.model.RouteChange;
import org.hackathon.buss.repository.RouteChangeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RouteChangeService {

    private final RouteChangeRepository routeChangeRepository;

    public Optional<RouteChange> findById(Long id) {
        return routeChangeRepository.findById(id);
    }

    public List<RouteChange> findAll() {
        return routeChangeRepository.findAll();
    }

    public void delete(Long id) {
        routeChangeRepository.delete(findById(id).orElseThrow());
    }

    public void deleteAll() {
        routeChangeRepository.deleteAll();
    }

}
