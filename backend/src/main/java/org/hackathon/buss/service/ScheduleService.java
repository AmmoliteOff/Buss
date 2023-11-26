package org.hackathon.buss.service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.hackathon.buss.enums.BusStatus;
import org.hackathon.buss.model.Bus;
import org.hackathon.buss.model.Route;
import org.hackathon.buss.model.RouteQueue;
import org.hackathon.buss.repository.BusRepository;
import org.hackathon.buss.repository.RouteRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final BusRepository busRepository;
    private final IntegrationService integrationService;
    private final RouteRepository routeRepository;
    private List<RouteQueue> routeQueueList;

    private LocalDateTime getCurrentTimeInterval() {
        var time = LocalDateTime.now();
        int minute = time.getMinute();
        int roundedMinute = minute - minute % 30;

        return time.withMinute(roundedMinute).withSecond(0).withNano(0);
    }

    private void updateBusStationsLoadInfo() {
        //
    }

    private void createSchedule() {

    }

    @PostConstruct
    private void init() {

        var routes = routeRepository.findAll();

        var busesWithoutRoute = new ArrayList<>(busRepository.findAll()
                .stream()
                .filter(bus -> bus.getRoute() == null)
                .toList());

        var busesWithRoute = busRepository.findAll()
                .stream()
                .filter(bus -> bus.getRoute() != null)
                .toList();


        for (Route route :
                routes) {
            routeQueueList.add(new RouteQueue(route));
        }

        for (RouteQueue routeQueue :
                routeQueueList) {
            var route = routeQueue.getRoute();
            var busesFromRoute = busesWithRoute
                    .stream()
                    .filter(bus -> bus.getRoute().equals(route))
                    .toList();

            for (Bus bus : busesFromRoute) {
                routeQueue.getBusQueue().add(bus);
            }

            while (routeQueue.getBusQueue().size() < route.getNorm() && !busesWithoutRoute.isEmpty()) {
                var bus = busesWithoutRoute.get(0);
                routeQueue.getBusQueue().add(bus);
                busesWithoutRoute.remove(bus);
                bus.setRoute(route);
            }
        }
       //createSchedule();
    }
}
