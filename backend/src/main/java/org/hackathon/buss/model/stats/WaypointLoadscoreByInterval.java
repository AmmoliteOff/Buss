package org.hackathon.buss.model.stats;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class WaypointLoadscoreByInterval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    private WaypointLoadscoreStatsByDay waypointLoadscoreStatsByDay;

    private int score;
}
