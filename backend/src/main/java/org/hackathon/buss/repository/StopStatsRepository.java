package org.hackathon.buss.repository;

import org.hackathon.buss.model.stats.StopStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StopStatsRepository extends JpaRepository<StopStats, Long> {
}
