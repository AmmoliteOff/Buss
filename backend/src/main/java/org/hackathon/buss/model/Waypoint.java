package org.hackathon.buss.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import org.hackathon.buss.model.stats.WaypointLoadscoreStatsByDay;
import org.hackathon.buss.util.view.DetailedInformation;
import org.hackathon.buss.util.view.NonDetailedInformation;

import java.util.List;

@Entity
@Data
@Table(name = "waypoints")
@JsonView({NonDetailedInformation.class, DetailedInformation.class})
public class Waypoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long waypointId;

    @ManyToOne
    @JsonView(DetailedInformation.class)
    private Route route;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonView(NonDetailedInformation.class)
    private Stop stop;

    @JsonIgnore
    @OneToMany(mappedBy = "waypoint", cascade = CascadeType.ALL)
    private List<WaypointLoadscoreStatsByDay> waypointLoadscoreStatsByDayList;
    private double longitude;
    private double latitude;

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Waypoint waypoint))
            return false;
        return waypoint.getWaypointId().equals(waypointId);
    }
}
