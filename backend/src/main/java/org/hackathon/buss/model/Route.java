package org.hackathon.buss.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hackathon.buss.util.view.DetailedInformation;
import org.hackathon.buss.util.view.NonDetailedInformation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "routes")
@JsonView({NonDetailedInformation.class, DetailedInformation.class})
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "opposite_route_id")
    @JsonView(DetailedInformation.class)
    private Route oppositeRoute;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String title;

    private int standartStep;

    @OneToMany (mappedBy = "route", fetch = FetchType.EAGER,  cascade = CascadeType.ALL)
    @JsonView(NonDetailedInformation.class)
    @OrderBy("waypointId")
    private List<Waypoint> route;

    @OneToMany(mappedBy = "route", fetch = FetchType.EAGER,  cascade = CascadeType.ALL)
    @JsonView(DetailedInformation.class)
    private List<Bus> buses;

    @OneToMany (mappedBy = "route", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("scheduleId ASC")
    @JsonView(DetailedInformation.class)
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany (mappedBy = "route", fetch = FetchType.EAGER,  cascade = CascadeType.ALL)
    @JsonView(DetailedInformation.class)
    private List<RouteChange> changes;

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Route anotherRoute)){
            return false;
        }
        return anotherRoute.getId().equals(this.id);
    }
}
