package org.hackathon.buss.dto;

import lombok.Data;
import org.hackathon.buss.model.Route;

@Data
public class RouteChangeDTO {
    private Route route;

    private String reason;
}
