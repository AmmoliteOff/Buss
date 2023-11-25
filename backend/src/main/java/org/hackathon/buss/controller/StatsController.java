package org.hackathon.buss.controller;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.model.stats.StopStats;
import org.hackathon.buss.repository.StopStatsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stats")
public class StatsController {

    private final StopStatsRepository stopStatsRepository;

    @GetMapping("/getStationsStats")
    public ResponseEntity<List<StopStats>> getStationsStats() {
        List<StopStats> stationStats = stopStatsRepository.findAll();
        return ResponseEntity.ok(stationStats);
    }
}
