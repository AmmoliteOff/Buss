package org.hackathon.buss.service;

import lombok.AllArgsConstructor;
import org.hackathon.buss.model.Route;
import org.hackathon.buss.repository.BusRepository;
import org.hackathon.buss.repository.RouteRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final BusRepository busRepository;
    private final IntegrationService integrationService;
    private final RouteRepository routeRepository;

    private LocalDateTime getCurrentTimeInterval() {
        var time = LocalDateTime.now();
        int minute = time.getMinute();
        int roundedMinute = minute - minute % 30;

        return time.withMinute(roundedMinute).withSecond(0).withNano(0);
    }

    private void updateBusStationsLoadInfo(){
        //
    }

    //@Scheduled()
    public void calculateSchedule(){
        updateBusStationsLoadInfo();
        var currentTimeInterval = getCurrentTimeInterval();
        var routes = routeRepository.findAll();
        for (Route route:
             routes) {



        }
    }
}
