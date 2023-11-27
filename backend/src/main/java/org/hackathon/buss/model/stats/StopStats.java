package org.hackathon.buss.model.stats;

import jakarta.persistence.*;
import lombok.Data;
import org.hackathon.buss.model.Stop;

import java.util.Map;

@Data
@Entity
public class StopStats {
    @Id
    private long busStationStatsId;

    @OneToOne
    Stop stop;

    private int peopleCount;

    private int lastBusPeopleCount;

//    @ElementCollection
//    @CollectionTable(name = "hourly_average_mapping", joinColumns = @JoinColumn(name = "bus_station_stats_id"))
//    @MapKeyColumn(name = "hour")
//    @Column(name = "average_count")
//    Map<Integer, StopStatsByDay> averagePeopleCount;
}
