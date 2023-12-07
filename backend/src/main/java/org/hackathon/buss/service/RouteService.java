package org.hackathon.buss.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hackathon.buss.dto.PosDTO;
import org.hackathon.buss.enums.WeatherCondition;
import org.hackathon.buss.model.Route;
import org.hackathon.buss.model.Stop;
import org.hackathon.buss.model.Waypoint;
import org.hackathon.buss.model.Weather;
import org.hackathon.buss.model.stats.*;
import org.hackathon.buss.repository.RouteRepository;
import org.hackathon.buss.repository.StopRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static org.hackathon.buss.util.Constants.*;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final IntegrationService integrationService;
    private final WaypointService waypointService;
    private final WeatherService weatherService;

    private final StopRepository stopRepository;

    public Optional<Route> findById(long id) {
        return routeRepository.findById(id);
    }
    public List<Route> findAll() {
        return routeRepository.findAll();
    }

    private void createRouteStats(Route route) {
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

    private void createWaypointAndStopsStats(Route route) {
        List<Waypoint> waypoints = waypointService.getAll();
        for (Waypoint waypoint:
                route.getWaypoints()) {

            waypoint.setWaypointLoadscoreStatsByDayList(new ArrayList<>());

            for(int i = 0; i<7; i++) {
                var waypointStatsByDay = new WaypointLoadscoreStatsByDay();
                waypointStatsByDay.setLoadscoreByIntervalList(new ArrayList<>());
                waypointStatsByDay.setWaypoint(waypoint);
                for(int j = 0; j < 48; j++){
                    var waypointStatsByInterval = new WaypointLoadscoreByInterval();
                    waypointStatsByInterval.setWaypointLoadscoreStatsByDay(waypointStatsByDay);
                    waypointStatsByInterval.setScore(integrationService.getScore(route));
                    waypointStatsByDay.getLoadscoreByIntervalList().add(waypointStatsByInterval);
                }
                waypoint.getWaypointLoadscoreStatsByDayList().add(waypointStatsByDay);
            }
            waypoint.setRoute(route);
            if(waypoint.getStop()!=null) {
                waypoint.getStop().setPeopleCount(integrationService.getPeopleCount());
                if(waypoint.getStop().getId() != null) {
                    waypoint.setStop(stopRepository.findById(waypoint.getStop().getId()).get());
                }
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
    public int getFullTime(Route route, int dayOfWeek, int timeInterval, int type){
        route = routeRepository.findById(route.getId()).get();
        double time = 0;
        var speed = (BUS_AVERAGE_SPEED/60.0);
        for(int i = 1; i<route.getWaypoints().size(); i++){
            time+= (DistanceService.calculateDistance(route.getWaypoints().get(i-1), route.getWaypoints().get(i))
                    /speed);
        }
        switch (type) {
            case 0: time = getStatTime(time, route, dayOfWeek, timeInterval);break;
            case 1: time = getRealTime(time, route);break;
            default: break;
        }
        return (int) time;
    }

    private int getRealTime(double time, Route route) {
        Weather weather = weatherService.getCurrentWeather();
        var weatherCoeff = 1;
        if(weather.getWeatherCondition().equals(WeatherCondition.CLEAR)) {
            weatherCoeff*= 1;
        } else if(weather.getWeatherCondition().equals(WeatherCondition.OVERCAST)) {
            weatherCoeff *= 1.2;
        } else if(weather.getWeatherCondition().equals(WeatherCondition.RAIN)) {
            weatherCoeff *= 1.5;
        } else if(weather.getWeatherCondition().equals(WeatherCondition.SNOW)) {
            weatherCoeff *= 1.8;
        } else {
            weatherCoeff *= 1.3;
        }
        return (int) (time * weatherCoeff * integrationService.getScore(route)/10 + 1);
    }

    private int getStatTime(double time, Route route, int dayOfWeek, int timeInterval) {
        var score = 0;
        for(int i = 1; i<route.getWaypoints().size(); i++){
            score += route.getWaypoints().get(i)
                    .getWaypointLoadscoreStatsByDayList()
                    .get(dayOfWeek)
                    .getLoadscoreByIntervalList()
                    .get(timeInterval)
                    .getScore();
        }
        var scoreCoeff = score/route.getWaypoints().size()/10 + 1;
        return (int) (time * 1.3 * scoreCoeff);
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
            value+= (int) Math.ceil(ri.getStop().getPeopleCount() *
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
        Waypoint waypoint = null;
        double distanceToWaypoint = 10000;
        for(int i = 0; i<route.getWaypoints().size(); i++){
            var dist = Math.sqrt(Math.pow((waypointCoords.getLatitude() - route.getWaypoints().get(i).getLatitude()),2) + Math.pow((waypointCoords.getLongitude() - route.getWaypoints().get(i).getLongitude()),2));
            if(dist<distanceToWaypoint){
                distanceToWaypoint = dist;
                waypoint = route.getWaypoints().get(i);
            }
        }

        boolean flag = false;

        for(int i = 0; i<route.getWaypoints().size(); i++){
            if(route.getWaypoints().get(i).equals(waypoint))
                flag = true;

            if(route.getWaypoints().get(i).getStop()!=null && route.getWaypoints().get(i).getStop().equals(B))
                flag = false;

            if(flag)
                distance+= DistanceService.calculateDistance(route.getWaypoints().get(i), route.getWaypoints().get(i+1));
        }

        return (int) Math.ceil(distance/(BUS_AVERAGE_SPEED/60.0)); //ADD COEFS
    }
}
