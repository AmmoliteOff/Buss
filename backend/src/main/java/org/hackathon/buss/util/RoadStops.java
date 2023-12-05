package org.hackathon.buss.util;

import lombok.Data;
import org.hackathon.buss.model.Stop;
@Data
public class RoadStops {
    private Stop stop;
    private boolean isReached = false;
}
