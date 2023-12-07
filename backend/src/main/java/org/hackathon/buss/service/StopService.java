package org.hackathon.buss.service;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.dto.PosDTO;
import org.hackathon.buss.dto.StopDTO;
import org.hackathon.buss.model.Route;
import org.hackathon.buss.model.Stop;
import org.hackathon.buss.model.Waypoint;
import org.hackathon.buss.model.stats.RouteStatsByDay;
import org.hackathon.buss.model.stats.RouteStatsByInterval;
import org.hackathon.buss.model.stats.RouteStatsByStop;
import org.hackathon.buss.repository.StopRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class StopService {

    private final StopRepository stopRepository;

    private final IntegrationService integrationService;
    private final RouteService routeService;
    private final RouteStatsByDayService routeStatsByDayService;

    public Optional<Stop> findById(long id) {
        return stopRepository.findById(id);
    }

    public List<Stop> findAll() {
        return stopRepository.findAll();
    }

    public Stop save(Stop stop) {
        stop.setPeopleCount(integrationService.getPeopleCount());
        return stopRepository.save(stop);
    }

    public void delete(Long id) {
        stopRepository.delete(findById(id).orElseThrow());
    }

    public Stop findByPos(PosDTO posDTO){
        Stop closestStop = null;
        var stops = stopRepository.findAll();
        var minDistance = 100000.0;
        for(Stop stop: stops){
            var wa = new Waypoint();
            var wb = new Waypoint();
            wa.setLatitude(posDTO.getLatitude());
            wa.setLongitude(posDTO.getLongitude());
            wb.setLongitude(stop.getLongitude());
            wb.setLatitude(stop.getLatitude());
            var distance = DistanceService.calculateDistance(wa, wb);
                if(distance<minDistance){
                    minDistance = distance;
                    closestStop = stop;
                }
            }
        return closestStop;
    }
    public Stop update(Long id, Stop newStop) {
        newStop.setId(id);
        return save(newStop);
    }

    public StopDTO getInfo(Long stopId, Long routeId) {
        Random random = new Random();
        Stop stop = findById(stopId).orElse(null);
        Route route = routeService.findById(routeId).orElseThrow();
        int dayOfWeek = LocalDateTime.now().getDayOfWeek().getValue();
        RouteStatsByDay routeStatsByDay = routeStatsByDayService.findByRoute(route, dayOfWeek * routeId).orElseThrow();
        List<RouteStatsByStop>  routeStatsByStopList = routeStatsByDay.getRouteStatsByStopList();
        List<RouteStatsByInterval> routeStatsByIntervalList = null;
        for (RouteStatsByStop routeStatsByStop : routeStatsByStopList) {
            if(routeStatsByStop.getStop().getId().equals(stopId)) {
                routeStatsByIntervalList = routeStatsByStop.getRouteStatsByIntervalList();
                break;
            }
        }
        StopDTO stopDTO = new StopDTO();
        stopDTO.setWaitingPeopleCount((int) Math.ceil(stop.getPeopleCount() * 0.1 + (0.7 - 0.1) * random.nextDouble()));
        stopDTO.setStopId(stopId);
        stopDTO.setCurrentPeopleCount(stop.getPeopleCount());
        stopDTO.setStats(routeStatsByIntervalList);
        return stopDTO;
    }

    public Optional<Stop> findByTitle (String title) {
        return stopRepository.findByTitle(title);
    }
}
