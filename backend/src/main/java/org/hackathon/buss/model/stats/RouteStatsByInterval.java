package org.hackathon.buss.model.stats;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import org.hackathon.buss.util.view.DetailedInformation;
import org.hackathon.buss.util.view.NonDetailedInformation;

@Entity
@Data
@JsonView(NonDetailedInformation.class)
public class RouteStatsByInterval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(DetailedInformation.class)
    private Long routeStatsByIntervalId;

    @ManyToOne
    @JsonView(DetailedInformation.class)
    private RouteStatsByStop routeStatsByStop;

    private int totalPeopleCount;

    private int peopleGoInBus;
}
