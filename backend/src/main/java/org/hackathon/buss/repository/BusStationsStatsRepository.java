package org.hackathon.buss.repository;

import org.hackathon.buss.model.stats.BusStationStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusStationsStatsRepository extends JpaRepository<BusStationStats, Long> {
}
