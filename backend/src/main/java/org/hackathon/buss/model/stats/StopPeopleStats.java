package org.hackathon.buss.model.stats;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hackathon.buss.model.Stop;

import java.util.HashMap;
import java.util.Map;

@Data
@Entity
public class StopPeopleStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stopPeopleStatsId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "stop_id")
    private Stop stop;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "stop_people_count",
            joinColumns = @JoinColumn(name = "stop_people_stats_id")
    )
    @MapKeyColumn(name = "time_interval")
    @Column(name = "people_count")
    private Map<Integer, Integer> peopleCountByTimeInterval = new HashMap<>();

}