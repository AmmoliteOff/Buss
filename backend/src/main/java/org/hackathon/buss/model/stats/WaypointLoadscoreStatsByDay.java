package org.hackathon.buss.model.stats;

import jakarta.persistence.*;
import lombok.Data;
import org.hackathon.buss.model.Waypoint;

import java.util.List;

@Entity
@Data
public class WaypointLoadscoreStatsByDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Waypoint waypoint;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "waypointLoadscoreStatsByDay")
    List<WaypointLoadscoreByInterval> loadscoreByIntervalList;
}