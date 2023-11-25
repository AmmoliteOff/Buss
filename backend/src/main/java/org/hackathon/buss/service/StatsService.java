package org.hackathon.buss.service;

import lombok.AllArgsConstructor;
import org.hackathon.buss.model.Bus;
import org.hackathon.buss.repository.BusStationsStatsRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatsService {
    private final BusStationsStatsRepository busStationsStatsRepository;
    public void updateNextStationInfo(Bus bus){

    }
}
