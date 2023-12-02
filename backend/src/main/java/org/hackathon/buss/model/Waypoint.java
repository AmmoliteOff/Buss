package org.hackathon.buss.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import org.hackathon.buss.util.view.DetailedInformation;
import org.hackathon.buss.util.view.NonDetailedInformation;

@Entity
@Data
@JsonView({NonDetailedInformation.class, DetailedInformation.class})
public class Waypoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long waypointId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonView(DetailedInformation.class)
    private Route route;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonView(NonDetailedInformation.class)
    private Stop stop;

    private int currentLoadScore;

    private double longitude;
    private double latitude;
}
