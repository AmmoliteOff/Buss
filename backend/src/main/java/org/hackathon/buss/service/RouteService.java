package org.hackathon.buss.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hackathon.buss.dto.PosDTO;
import org.hackathon.buss.model.Route;
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
    private final WaypointService waypointService;
    public Optional<Route> findById(long id) {
        return routeRepository.findById(id);
    }
    public List<Route> findAll() {
        return routeRepository.findAll();
    }

    private void createRouteStats(Route route){
        List<Waypoint> waypoints = waypointService.getAll();
        Random random = new Random();
        if(route.getRouteStatsByWeek() == null) {
            route.setRouteStatsByWeek(new ArrayList<>());
            for(int i = 0; i<7; i++) {
                RouteStatsByDay routeStatsByDay = new RouteStatsByDay();
                routeStatsByDay.setRouteStatsByStopList(new ArrayList<>());
                routeStatsByDay.setRoute(route);
                for (Waypoint waypoint : route.getWaypoints()) {
                    if (waypoint.getStop() != null) {
                        Stop stop = null;
                        for(Waypoint w : waypoints) {
                            if(Objects.equals(w.getLongitude(), waypoint.getLongitude())
                                    && Objects.equals(w.getLatitude(), waypoint.getLatitude())) {
                                stop = w.getStop();
                            }
                        }
                        if(stop == null) {
                            stop = waypoint.getStop();
                        }
                        RouteStatsByStop routeStatsByStop = new RouteStatsByStop();
                        routeStatsByStop.setRouteStatsByIntervalList(new ArrayList<>());
                        routeStatsByStop.setStop(stop);
                        routeStatsByStop.setRouteStatsByDay(routeStatsByDay);
                        for (int j = 0; j < 48; j++) {
                            RouteStatsByInterval routeStatsByInterval = new RouteStatsByInterval();
                            var value = random.nextInt(0, 25);
                            routeStatsByInterval.setPeopleGoInBus(value);
                            routeStatsByInterval.setTotalPeopleCount(random.nextInt(value, 40));
                            routeStatsByInterval.setRouteStatsByStop(routeStatsByStop);
                            routeStatsByStop.getRouteStatsByIntervalList().add(routeStatsByInterval);
                        }
                        routeStatsByDay.getRouteStatsByStopList().add(routeStatsByStop);
                    }
                }
                route.getRouteStatsByWeek().add(routeStatsByDay);
            }
        }
    }

    private void createWaypointAndStopsStats(Route route){
        List<Waypoint> waypoints = waypointService.getAll();
        Random random = new Random();
        for (Waypoint waypoint:
                route.getWaypoints()) {

            waypoint.setWaypointLoadscoreStatsByDayList(new ArrayList<>());

            for(int i = 0; i<7;i++){
                var waypointStatsByDay = new WaypointLoadscoreStatsByDay();
                waypointStatsByDay.setLoadscoreByIntervalList(new ArrayList<>());
                waypointStatsByDay.setWaypoint(waypoint);
                for(int j = 0; j < 48; j++){
                    var waipointStatsByIntreval = new WaypointLoadscoreByInterval();
                    waipointStatsByIntreval.setWaypointLoadscoreStatsByDay(waypointStatsByDay);
                    waipointStatsByIntreval.setScore(random.nextInt(0,10));
                    waypointStatsByDay.getLoadscoreByIntervalList().add(waipointStatsByIntreval);
                }
                waypoint.getWaypointLoadscoreStatsByDayList().add(waypointStatsByDay);
            }

            waypoint.setRoute(route);
            if(waypoint.getStop()!=null) {
                for(Waypoint w : waypoints) {
                    if(Objects.equals(w.getLongitude(), waypoint.getLongitude())
                            && Objects.equals(w.getLatitude(), waypoint.getLatitude())) {
                        waypoint.setStop(w.getStop());
                        break;
                    }
                }

                waypoint.getStop().setLatitude(waypoint.getLatitude());
                waypoint.getStop().setLongitude(waypoint.getLongitude());
            }
        }
    }

    public List<Route> save(Route route1, Route route2) {
        var result = new ArrayList<Route>();
        createRouteStats(route1);
        createRouteStats(route2);

        createWaypointAndStopsStats(route1);
        createWaypointAndStopsStats(route2);

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
    @Transactional
    public int getNorm(Route route, int dayOfWeek, int timeInterval){
        route = routeRepository.findById(route.getId()).get();
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

    public void update(Route route){
        routeRepository.save(route);
    }
    @Transactional
    public int getFullTime(Route route, int dayOfWeek, int timeInterval){
        route = routeRepository.findById(route.getId()).get();
        double time = 0;
        var speed = (BUS_AVERAGE_SPEED/60.0);
        for(int i = 1; i<route.getWaypoints().size(); i++){
            var scoreCoef = route.getWaypoints().get(i)
                    .getWaypointLoadscoreStatsByDayList()
                    .get(dayOfWeek)
                    .getLoadscoreByIntervalList()
                    .get(timeInterval)
                    .getScore()/10 + 1;
            time+= (DistanceService.calculateDistance(route.getWaypoints().get(i-1), route.getWaypoints().get(i))
                    /speed) * scoreCoef;
        }
        return (int) time;
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
    @Transactional
    public int getRealNorm(Route route, int timeInterval){
        int value = 0;
        route = routeRepository.findById(route.getId()).get();
        var routeStats = route.getRouteStatsByWeek().get(LocalDateTime
                .now()
                .getDayOfWeek()
                .getValue());
        for(RouteStatsByStop ri : routeStats.getRouteStatsByStopList()){
            value+= (int) Math.ceil(integrationService.getPeopleCount(ri.getStop()) *
                    (ri.getRouteStatsByIntervalList()
                            .get(timeInterval)
                            .getPeopleGoInBus())/((double)ri.getRouteStatsByIntervalList()
                    .get(timeInterval)
                    .getTotalPeopleCount()));
        }
        var norm = (int) Math.ceil(value/BUS_CAPACITY);
        return Math.max(norm, INTERVAL / route.getNormalStep());
    }

    public int getAlmostRealWaypointToStopTime(Route route, PosDTO waypointCoords, Stop B){
        double distance = 0;
        int waypointIndex = 0;
        double distanceToWaypoint = 10000;
        for(int i = 0; i<route.getWaypoints().size(); i++){
            var dist = Math.sqrt(Math.pow((waypointCoords.getLatitude() - route.getWaypoints().get(i).getLatitude()),2) + Math.pow((waypointCoords.getLongitude() - route.getWaypoints().get(i).getLongitude()),2));
            if(dist<distanceToWaypoint){
                distanceToWaypoint = dist;
                waypointIndex = i;
            }
        }
        var waypoint = route.getWaypoints().get(waypointIndex);
        while(waypoint.getStop().equals(B)){
            distance+= DistanceService.calculateDistance(waypoint, route.getWaypoints().get(waypointIndex+1));
            waypointIndex++;
            waypoint = route.getWaypoints().get(waypointIndex);
        }

        return (int) Math.ceil(distance/(BUS_AVERAGE_SPEED/60.0)); //ADD COEFS
    }
}
