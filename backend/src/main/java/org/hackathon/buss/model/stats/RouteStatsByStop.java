package org.hackathon.buss.model.stats;

import jakarta.persistence.*;
import lombok.Data;
import org.hackathon.buss.model.Stop;

import java.util.List;

@Entity
@Data
public class RouteStatsByStop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Stop stop;

    @ManyToOne
    private RouteStatsByDay routeStatsByDay;

    @OneToMany(mappedBy = "routeStatsByStop", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RouteStatsByInterval> routeStatsByIntervalList;
}
