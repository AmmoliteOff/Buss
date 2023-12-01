package org.hackathon.buss.service;

import org.hackathon.buss.model.Waypoint;
import org.springframework.stereotype.Service;

@Service
public class DistanceService {
    // Радиус Земли в километрах
    private static final double EARTH_RADIUS = 6371.0;
    public static double calculateDistance(Waypoint A, Waypoint B) {
        double lat1Rad = Math.toRadians(A.getLatitude());
        double lon1Rad = Math.toRadians(A.getLongitude());
        double lat2Rad = Math.toRadians(B.getLatitude());
        double lon2Rad = Math.toRadians(B.getLongitude());

        double dlat = lat2Rad - lat1Rad;
        double dlon = lon2Rad - lon1Rad;

        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}
