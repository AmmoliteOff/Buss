package org.hackathon.buss.service;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.dto.RouteChangeDTO;
import org.hackathon.buss.model.Route;
import org.hackathon.buss.model.RouteChange;
import org.hackathon.buss.model.Stop;
import org.hackathon.buss.model.Waypoint;
import org.hackathon.buss.model.stats.*;
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

    public List<Route> save(Route route1, Route route2) {
        var result = new ArrayList<Route>();
        route1.setRouteStatsByWeek(new ArrayList<>());
        route2.setRouteStatsByWeek(new ArrayList<>());
        Random random = new Random();

        route1.setRouteStatsByWeek(new ArrayList<>());
        route2.setRouteStatsByWeek(new ArrayList<>());

        for(int i = 0; i<7; i++) {
            if(route1.getRouteStatsByWeek() == null) {
                RouteStatsByDay routeStatsByDay = new RouteStatsByDay();
                routeStatsByDay.setRouteStatsByStopList(new ArrayList<>());
                routeStatsByDay.setRoute(route1);
                for (Waypoint waypoint : route1.getWaypoints()) {
                    if (waypoint.getStop() != null) {
                        RouteStatsByStop routeStatsByStop = new RouteStatsByStop();
                        routeStatsByStop.setRouteStatsByIntervalList(new ArrayList<>());
                        routeStatsByStop.setStop(waypoint.getStop());
                        routeStatsByStop.setRouteStatsByDay(routeStatsByDay);
                        for (int j = 0; j < 48; j++) {
                            RouteStatsByInterval routeStatsByInterval = new RouteStatsByInterval();
                            routeStatsByInterval.setPeopleGoInBus(random.nextInt(0, 25));
                            routeStatsByInterval.setRouteStatsByStop(routeStatsByStop);
                            routeStatsByStop.getRouteStatsByIntervalList().add(routeStatsByInterval);
                        }
                        routeStatsByDay.getRouteStatsByStopList().add(routeStatsByStop);
                    }
                }
                route1.getRouteStatsByWeek().add(routeStatsByDay);
            }
        }

        for(int i = 0; i<7; i++) {
            if(route2.getRouteStatsByWeek() == null) {
                RouteStatsByDay routeStatsByDay = new RouteStatsByDay();
                routeStatsByDay.setRouteStatsByStopList(new ArrayList<>());
                routeStatsByDay.setRoute(route2);
                for (Waypoint waypoint : route2.getWaypoints()) {
                    if (waypoint.getStop() != null) {
                        RouteStatsByStop routeStatsByStop = new RouteStatsByStop();
                        routeStatsByStop.setRouteStatsByIntervalList(new ArrayList<>());
                        routeStatsByStop.setStop(waypoint.getStop());
                        routeStatsByStop.setRouteStatsByDay(routeStatsByDay);
                        for (int j = 0; j < 48; j++) {
                            RouteStatsByInterval routeStatsByInterval = new RouteStatsByInterval();
                            routeStatsByInterval.setPeopleGoInBus(random.nextInt(0, 25));
                            routeStatsByInterval.setRouteStatsByStop(routeStatsByStop);
                            routeStatsByStop.getRouteStatsByIntervalList().add(routeStatsByInterval);
                        }
                        routeStatsByDay.getRouteStatsByStopList().add(routeStatsByStop);
                    }
                }
                route2.getRouteStatsByWeek().add(routeStatsByDay);
            }
        }

        for (Waypoint waypoint:
             route1.getWaypoints()) {
            waypoint.setRoute(route1);
            if(waypoint.getStop()!=null){
                var stop = stopService.findByTitle(waypoint.getStop().getTitle());
                stop.ifPresent(waypoint::setStop);
                if(stop.isEmpty()){
                    var currentStop = waypoint.getStop();
                    currentStop.setStatsByWeek(new ArrayList<>());
                    for(int i = 0; i<7; i++){
                        var stopStats = new StopStatsByDay();
                        stopStats.setStopStatsByIntervalList(new ArrayList<>());
                        stopStats.setStop(currentStop);
                        for(int j = 0; j<48; j++){
                            var stopStatsByInterval = new StopStatsByInterval();
                            stopStatsByInterval.setStopStatsByDay(stopStats);
                            stopStatsByInterval.setPeopleCount(random.nextInt(1,50));
                            stopStats.getStopStatsByIntervalList().add(stopStatsByInterval);
                        }
                        currentStop.getStatsByWeek().add(stopStats);
                    }
                }
            }
        }

        for (Waypoint waypoint:
                route2.getWaypoints()) {
            waypoint.setRoute(route2);
            if(waypoint.getStop()!=null){
                var stop = stopService.findByTitle(waypoint.getStop().getTitle());
                stop.ifPresent(waypoint::setStop);
                if(stop.isEmpty()){
                    var currentStop = waypoint.getStop();
                    currentStop.setStatsByWeek(new ArrayList<>());
                    for(int i = 0; i<7; i++){
                        var stopStats = new StopStatsByDay();
                        stopStats.setStopStatsByIntervalList(new ArrayList<>());
                        stopStats.setStop(currentStop);
                        for(int j = 0; j<48; j++){
                            var stopStatsByInterval = new StopStatsByInterval();
                            stopStatsByInterval.setStopStatsByDay(stopStats);
                            stopStatsByInterval.setPeopleCount(random.nextInt(1,50));
                            stopStats.getStopStatsByIntervalList().add(stopStatsByInterval);
                        }
                        currentStop.getStatsByWeek().add(stopStats);
                    }
                }
            }
        }

        var r1= routeRepository.save(route1);
        route2.setOppositeRouteId(r1.getId());
        var r2 = routeRepository.save(route2);
        r1.setOppositeRouteId(r2.getId());
        routeRepository.save(r1);
        result.add(r1);
        result.add(r2);
        return result;
    }

    public void delete(Long id) {
        routeRepository.delete(findById(id).orElseThrow());
    }

    public int getNorm(Route route, int dayOfWeek, int timeInterval){
        int value = 0;
        var dayStats = route.getRouteStatsByWeek().get(dayOfWeek-1);
        for(var routeStatsByStop: dayStats.getRouteStatsByStopList()){
            value+= routeStatsByStop.getRouteStatsByIntervalList().get(timeInterval).getPeopleGoInBus();
        }
        var norm = (int) Math.ceil(value/BUS_CAPACITY);
        return Math.max(norm, INTERVAL / route.getNormalStep());
    }

    public double getFullDistance(Route route){
        double distance = 0;
        for(int i = 1; i<route.getWaypoints().size(); i++){
            distance += DistanceService.calculateDistance(route.getWaypoints().get(i-1), route.getWaypoints().get(i));
        }
        return distance;
    }

    public int getFullTime(Route route, int dayOfWeek, int timeInterval){
        double distance = 0;
        for(int i = 1; i<route.getWaypoints().size(); i++){
            distance+= DistanceService.calculateDistance(route.getWaypoints().get(i-1), route.getWaypoints().get(i));
        }
        return (int) Math.ceil(distance/(BUS_AVERAGE_SPEED/60.0));
    }
    public int getAverageStopToStopTime(int time, Route route, Stop A, Stop B){
       double distance = 0;

       int aIndex = 0;

       while(!route.getWaypoints().get(aIndex).getStop().equals(A)){
           aIndex++;
       }

        while(!route.getWaypoints().get(aIndex).getStop().equals(B)){
            distance+=DistanceService.calculateDistance(route.getWaypoints().get(aIndex), route.getWaypoints().get(aIndex+1));
            aIndex++;
        }

       return (int) Math.ceil(distance/(BUS_AVERAGE_SPEED/60.0)); //ADD COEFS
    }

    public int getRealNorm(Route route){
        int value = 0;
        for (Waypoint waypoint:
                route.getWaypoints()) {
            if(waypoint.getStop()!=null){
                value += integrationService.getPeopleCount(waypoint.getStop()); //STATS PERCENT
            }
        }
        var norm = (int) Math.ceil(value/BUS_CAPACITY);
        return Math.max(norm, INTERVAL / route.getNormalStep());
    }

    public int getAlmostRealStopToStopTime(Route route, Stop A, Stop B){
        double distance = 0;

        int aIndex = 0;

        while(!route.getWaypoints().get(aIndex).getStop().equals(A)){
            aIndex++;
        }

        while(!route.getWaypoints().get(aIndex).getStop().equals(B)){
            distance+=DistanceService.calculateDistance(route.getWaypoints().get(aIndex), route.getWaypoints().get(aIndex+1));
            aIndex++;
        }

        return (int) Math.ceil(distance/(BUS_AVERAGE_SPEED/60.0)); //ADD COEFS
    }
}
