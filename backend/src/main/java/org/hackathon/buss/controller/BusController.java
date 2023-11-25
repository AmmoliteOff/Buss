package org.hackathon.buss.controller;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.model.Bus;
import org.hackathon.buss.service.BusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bus")
public class BusController {

    private final BusService busService;

    @GetMapping()
    public ResponseEntity<Bus> getBus(long busId) {
        Bus bus = busService.findBus(busId).orElse(null);
        return ResponseEntity.ok(bus);
    }
}
