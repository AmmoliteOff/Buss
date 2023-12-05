package org.hackathon.buss.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hackathon.buss.model.Stop;
import org.hackathon.buss.model.Waypoint;

@Data
@Entity
public class RoadStops {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Bus bus;

    @ManyToOne
    private Waypoint waypoint;
    private boolean isReached = false;
}
