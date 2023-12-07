package org.hackathon.buss.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.hackathon.buss.model.stats.RouteStatsByInterval;
import org.hackathon.buss.util.view.NonDetailedInformation;

import java.util.List;

@Data
@JsonView(NonDetailedInformation.class)
public class StopDTO {
    private Long stopId;

    private int currentPeopleCount;

    private int waitingPeopleCount;

    List<RouteStatsByInterval> stats;


}