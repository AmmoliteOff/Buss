package org.hackathon.buss.repository;

import org.hackathon.buss.model.Bus;
import org.hackathon.buss.model.RoadStops;
import org.hackathon.buss.model.Stop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoadStopsRepository extends JpaRepository<RoadStops, Long> {
    public RoadStops findByStop(Stop stop);
    public List<RoadStops> findAllByBus(Bus bus);
}
