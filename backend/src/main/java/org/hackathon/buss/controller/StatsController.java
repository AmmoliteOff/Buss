package org.hackathon.buss.controller;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.model.stats.BusStationStats;
import org.hackathon.buss.repository.BusStationsStatsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stats")
public class StatsController {

    private final BusStationsStatsRepository busStationsStatsRepository;

    @GetMapping("/getStationsStats")
    public ResponseEntity<List<BusStationStats>> getStationsStats() {
        List<BusStationStats> stationStats = busStationsStatsRepository.findAll();
        return ResponseEntity.ok(stationStats);
    }
}
