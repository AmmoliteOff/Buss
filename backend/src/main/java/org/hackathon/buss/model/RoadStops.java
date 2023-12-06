package org.hackathon.buss.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import org.hackathon.buss.util.view.DetailedInformation;
import org.hackathon.buss.util.view.NonDetailedInformation;

@Data
@Entity
@JsonView({NonDetailedInformation.class, DetailedInformation.class})
public class RoadStops {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Bus bus;

//    @ManyToOne
//    private Waypoint waypoint;

    @ManyToOne
    private Stop stop;

    private boolean isReached = false;
    private int orderInList;
}
