package org.hackathon.buss.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Data;
import org.hackathon.buss.model.Route;
import org.hackathon.buss.util.view.NonDetailedInformation;

@Data
@Builder
@JsonView({NonDetailedInformation.class})
public class RouteDTO {
    private Route route;

    private Long oppositeRouteId;
}
