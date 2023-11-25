package org.hackathon.buss.service;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.model.Bus;
import org.hackathon.buss.repository.BusRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BusService {

    private final BusRepository busRepository;

    public Optional<Bus> findBus(long id) {
        return busRepository.findById(id);
    }
}
