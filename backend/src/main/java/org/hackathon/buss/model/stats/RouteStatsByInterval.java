package org.hackathon.buss.model.stats;

import jakarta.persistence.*;
import lombok.Data;
import org.hackathon.buss.model.Stop;

@Entity
@Data
public class RouteStatsByInterval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeStatsByIntervalId;

    @ManyToOne
    private RouteStatsByStop routeStatsByStop;

    private int peopleGoInBus;
}
