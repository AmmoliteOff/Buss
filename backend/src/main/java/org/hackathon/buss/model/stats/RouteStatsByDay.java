package org.hackathon.buss.model.stats;

import jakarta.persistence.*;
import lombok.Data;
import org.hackathon.buss.model.Route;

import java.util.List;

@Entity
@Data
public class RouteStatsByDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeStatsByDayId;

    @ManyToOne
    private Route route;

    @OneToMany(mappedBy = "routeStatsByDay", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RouteStatsByStop> routeStatsByStopList;
}
