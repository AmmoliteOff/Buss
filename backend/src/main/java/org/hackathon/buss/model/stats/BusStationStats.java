package org.hackathon.buss.model.stats;

import jakarta.persistence.*;
import lombok.Data;
import org.hackathon.buss.model.BusStation;

import java.util.Map;

@Data
@Entity
public class BusStationStats {
    @Id
    private long busStationStatsId;

    @OneToOne
    BusStation busStation;

    private int peopleCount;

    private int lastBusPeopleCount;

    @ElementCollection
    @CollectionTable(name = "hourly_average_mapping", joinColumns = @JoinColumn(name = "bus_station_stats_id"))
    @MapKeyColumn(name = "hour")
    @Column(name = "average_count")
    Map<Integer, Double> averagePeopleCount;
}
