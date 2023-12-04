package org.hackathon.buss.service;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.dto.RouteChangeDTO;
import org.hackathon.buss.model.Route;
import org.hackathon.buss.model.RouteChange;
import org.hackathon.buss.model.Stop;
import org.hackathon.buss.model.Waypoint;
import org.hackathon.buss.model.stats.StopPeopleStats;
import org.hackathon.buss.repository.RouteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static org.hackathon.buss.util.Constants.*;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final IntegrationService integrationService;
    private final StopService stopService;
    public Optional<Route> findById(long id) {
        return routeRepository.findById(id);
    }

    public List<Route> findAll() {
        return routeRepository.findAll();
    }

    public Route save(Route route) {

        for(Waypoint waypoint : route.getRoute()) {
            if(waypoint.getStop() != null && stopService.findByTitle(waypoint.getStop().getTitle()).isPresent()) {
                waypoint.setStop(stopService.findByTitle(waypoint.getStop().getTitle()).get());
            }
            waypoint.setRoute(route);
        }
        for(Waypoint waypoint : route.getOppositeRoute().getRoute()) {
            if(waypoint.getStop() != null && stopService.findByTitle(waypoint.getStop().getTitle()).isPresent()) {
                waypoint.setStop(stopService.findByTitle(waypoint.getStop().getTitle()).get());
            }
            waypoint.setRoute(route.getOppositeRoute());
        }
        route.getOppositeRoute().setOppositeRoute(route);
        var resultRoute = routeRepository.save(route);
        var waypoints = resultRoute.getRoute();
        waypoints.addAll(resultRoute.getOppositeRoute().getRoute());
        Random random = new Random();
        for (Waypoint waypoint:
             waypoints) {
            if(waypoint.getStop()!=null && waypoint.getStop().getPeopleStatsMap().isEmpty()) {
                    var stop = waypoint.getStop();
                    Map<Integer, StopPeopleStats> map = new HashMap<>();
                    stop.setPeopleStatsMap(map);
                    for (int i = 0; i < 7; i++) {
                        StopPeopleStats stopPeopleStats = new StopPeopleStats();
                        stopPeopleStats.setStop(stop);
                        Map<Integer, Integer> secondMap = new HashMap<>();
                        for (int j = 1; j < 49; j++) {
                            secondMap.put(j, random.nextInt(1, 35));
                        }
                        stopPeopleStats.setPeopleCountByTimeInterval(secondMap);
                        map.put(i + 1, stopPeopleStats);
                    }
                    stopService.save(stop);
            }
        }
        return resultRoute;
    }

    public void delete(Long id) {
        routeRepository.delete(findById(id).orElseThrow());
    }
    public Route update(Long id, RouteChangeDTO routeChangeDTO) {
        Route route = findById(id).orElseThrow();
        RouteChange routeChange = new RouteChange();
        routeChange.setTime(LocalDateTime.now());
        routeChange.setReason(routeChangeDTO.getReason());
        routeChange.setRoute(route);
        route.getOppositeRoute().getChanges().add(routeChange);
        routeRepository.save(route.getOppositeRoute());
        route.getChanges().add(routeChange);
        return routeRepository.save(route);
    }

    public int getNorm(Route route, int dayOfWeek, int timeInterval){
        int value = 0;
        for (Waypoint waypoint:
             route.getRoute()) {
            if(waypoint.getStop()!=null){
                value += waypoint.getStop()
                        .getPeopleStatsMap()
                        .get(dayOfWeek)
                        .getPeopleCountByTimeInterval()
                        .get(timeInterval);
            }
        }
        var norm = (int) Math.ceil(value/BUS_CAPACITY);
        return Math.max(norm, INTERVAL / route.getStandartStep());
    }

    public double getFullDistance(Route route){
        double distance = 0;
        for(int i = 1; i<route.getRoute().size(); i++){
            distance += DistanceService.calculateDistance(route.getRoute().get(i-1), route.getRoute().get(i));
        }
        return distance;
    }

    public int getAverageStopToStopTime(int time, Route route, Stop A, Stop B){
       double distance = 0;

       int aIndex = 0;

       while(!route.getRoute().get(aIndex).getStop().equals(A)){
           aIndex++;
       }

        while(!route.getRoute().get(aIndex).getStop().equals(B)){
            distance+=DistanceService.calculateDistance(route.getRoute().get(aIndex), route.getRoute().get(aIndex+1));
            aIndex++;
        }

       return (int) Math.ceil(distance/(BUS_AVERAGE_SPEED/60.0)); //ADD COEFS
    }

    public int getRealNorm(Route route){
        int value = 0;
        for (Waypoint waypoint:
                route.getRoute()) {
            if(waypoint.getStop()!=null){
                value += integrationService.getPeopleCount(waypoint.getStop());
            }
        }
        var norm = (int) Math.ceil(value/BUS_CAPACITY);
        return Math.max(norm, INTERVAL / route.getStandartStep());
    }

    public int getAlmostRealStopToStopTime(Route route, Stop A, Stop B){
        double distance = 0;

        int aIndex = 0;

        while(!route.getRoute().get(aIndex).getStop().equals(A)){
            aIndex++;
        }

        while(!route.getRoute().get(aIndex).getStop().equals(B)){
            distance+=DistanceService.calculateDistance(route.getRoute().get(aIndex), route.getRoute().get(aIndex+1));
            aIndex++;
        }

        return (int) Math.ceil(distance/(BUS_AVERAGE_SPEED/60.0)); //ADD COEFS
    }
}
