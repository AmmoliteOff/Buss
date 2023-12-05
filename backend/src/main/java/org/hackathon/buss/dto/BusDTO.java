package org.hackathon.buss.dto;

import lombok.Data;

@Data
public class BusDTO {
    private Long busId;
    private double charge;
    private double longitude;
    private double latitude;
    private boolean isCharging = false;
}
