package org.hackathon.buss.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Waypoint {
    @Id
    private Long waypointId;

    @ManyToOne
    Route route;

    @ManyToOne
    Stop stop;

    private double longitude;
    private double latitude;
}
