package org.hackathon.buss.service;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.model.Bus;
import org.hackathon.buss.repository.BusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BusService {

    private final BusRepository busRepository;

    public Optional<Bus> findById(long id) {
        return busRepository.findById(id);
    }

    public List<Bus> findAll() {
        return busRepository.findAll();
    }

    public Bus save(Bus bus) {
        return busRepository.save(bus);
    }

    public void delete(Long id) {
        busRepository.delete(findById(id).orElseThrow());
    }
}
