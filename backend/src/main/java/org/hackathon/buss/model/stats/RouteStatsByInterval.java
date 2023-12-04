package org.hackathon.buss.model.stats;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class RouteStatsByInterval {
    @Id
    private Long routeStatsByIntervalId;

    @ManyToOne
    private RouteStatsByDay routeStatsByDay;

    private int peopleGoInBus;
}
