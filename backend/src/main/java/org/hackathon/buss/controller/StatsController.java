package org.hackathon.buss.controller;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.model.Bus;
import org.hackathon.buss.model.stats.BusStationStats;
import org.hackathon.buss.repository.BusStationsStatsRepository;
import org.hackathon.buss.service.BusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stats")
public class StatsController {

    private final BusStationsStatsRepository busStationsStatsRepository;

    @GetMapping("/getStationsStats")
    public ResponseEntity<BusStationStats> getStationsStats() {
        BusStationStats stationStats = busStationsStatsRepository.findById(1L).orElse(null);
        return ResponseEntity.ok(stationStats);
    }
}
