package org.hackathon.buss.model.stats;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Map;
@Data
@Entity

public class StopStatsByDay {
    @Id
    private Long stopStatByDayId;

    @ElementCollection
    @CollectionTable(name = "hourly_average_mapping", joinColumns = @JoinColumn(name = "bus_station_stats_id"))
    @MapKeyColumn(name = "hour")
    @Column(name = "average_count")
    Map<Integer, Double> averagePeopleCount;
}
