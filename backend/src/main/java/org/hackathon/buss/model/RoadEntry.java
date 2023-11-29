package org.hackathon.buss.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RoadEntry {
    private Bus bus;
    private LocalDateTime arrivalTime;
}
