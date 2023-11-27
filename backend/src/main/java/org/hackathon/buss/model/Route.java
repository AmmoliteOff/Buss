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

    private int norm;

    private double loadFactor;

    @OneToMany
    private List<Stop> stops;

    @Transient
    @JsonIgnore
    private List<Bus> buses;

    @OneToMany
    private List<Schedule> schedules = new ArrayList<>();

    @Transient
//    @OneToMany(mappedBy = "route", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<RouteChange> changes;

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Route anotherRoute)){
            return false;
        }
        return anotherRoute.getId().equals(this.id);
    }
}
