package org.hackathon.buss.model.stats;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RouteStatsByInterval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeStatsByIntervalId;

    @ManyToOne
    private RouteStatsByDay routeStatsByDay;

    private int peopleGoInBus;
}
