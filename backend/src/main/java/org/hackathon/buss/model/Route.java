package org.hackathon.buss.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "routes")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JsonIgnore
    private Route oppositeRoute;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String title;

    private int standartStep;

    @OneToMany (mappedBy = "route", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Waypoint> route;

    @OneToMany(mappedBy = "route", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Bus> buses;

    @OneToMany (mappedBy = "route", fetch = FetchType.EAGER)
    @OrderBy("scheduleId ASC")
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany (mappedBy = "route")
    private List<RouteChange> changes;

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Route anotherRoute)){
            return false;
        }
        return anotherRoute.getId().equals(this.id);
    }
}
