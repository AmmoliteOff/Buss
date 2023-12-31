package org.hackathon.buss.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hackathon.buss.enums.RouteDirection;
import org.hackathon.buss.model.stats.RouteStatsByDay;
import org.hackathon.buss.util.view.DetailedInformation;
import org.hackathon.buss.util.view.NonDetailedInformation;
import org.springframework.format.annotation.DateTimeFormat;

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

    private Long oppositeRouteId;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;

    private String title;

    private int normalStep;

    @Enumerated(EnumType.STRING)
    private RouteDirection direction;

    @OneToMany (mappedBy = "route", fetch = FetchType.EAGER,  cascade = CascadeType.ALL)
    @JsonView(NonDetailedInformation.class)
    @OrderBy("waypointId")
    private List<Waypoint> waypoints;

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

    @JsonView(DetailedInformation.class)
    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    private List<RouteStatsByDay> routeStatsByWeek;

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Route anotherRoute)){
            return false;
        }
        return anotherRoute.getId().equals(this.id);
    }
}
