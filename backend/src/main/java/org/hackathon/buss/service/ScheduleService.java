package org.hackathon.buss.service;

import lombok.AllArgsConstructor;
import org.hackathon.buss.model.Route;
import org.hackathon.buss.model.Schedule;
import org.hackathon.buss.model.ScheduleConstructor;
import org.hackathon.buss.model.ScheduleEntry;
import org.hackathon.buss.repository.BusRepository;
import org.hackathon.buss.repository.RouteRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final int interval = 30;
    private final BusRepository busRepository;
    private final IntegrationService integrationService;
    private final RouteService routeService;
    private final RouteRepository routeRepository;
    private List<ScheduleConstructor> scheduleConstructorList = new ArrayList<>();
    private int getCurrentTimeIntervalInt() {
        var time = LocalDateTime.now();
        int minute = time.getMinute();
        int roundedMinute = minute - minute % 30;

        var c = time.withMinute(roundedMinute).withSecond(0).withNano(0);
        var minutes = c.getHour()*60+c.getMinute();
        return minutes/interval;
    }

    private LocalDateTime getCurrentTimeInterval() {
        var time = LocalDateTime.now();
        int minute = time.getMinute();
        int roundedMinute = minute - minute % 30;

        return time.withMinute(roundedMinute).withSecond(0).withNano(0);
    }

    private int getCurrentDayOfWeek(){
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        return dayOfWeek.getValue();
    }
    private void updateBusStationsLoadInfo() {
        //
    }

    private void createScheduleBasedOnStats() {
        var routes = routeRepository.findAll();
        var skipList = new ArrayList<Route>();
        for (Route a:
             routes) {
            if(!skipList.contains(a)) {
                var b = a.getOppositeRoute();
                skipList.add(b);
                scheduleConstructorList.add(new ScheduleConstructor(a, b));
            }
        }

        var time = getCurrentTimeIntervalInt(); //START INTERVAL, NOT CURRENT
        var currentTimeInterval = getCurrentTimeInterval(); //TOMORROW DATE, NOT CURRENT
        var dayOfWeek = getCurrentDayOfWeek();
        for(ScheduleConstructor sc: scheduleConstructorList){
            var AN = routeService.getNorm(sc.getA(), dayOfWeek, time);
            var BN = routeService.getNorm(sc.getB(), dayOfWeek, time);

            var priorityRoute = AN>BN?sc.getA():sc.getB();
            var nonPriorityRoute = AN>BN?sc.getB():sc.getA();

            var priorityNnext = routeService.getNorm(priorityRoute, dayOfWeek, time+1);

            nonPriorityRoute.getSchedules().add(new Schedule());
            var nonPriorityRouteSchedule = nonPriorityRoute.getSchedules().get(0);

            var priorityIntervalEnd = currentTimeInterval.plusMinutes(interval);

            var startTime = (priorityIntervalEnd.plusMinutes(interval/priorityNnext))
                    .minusMinutes(routeService.getAverageRoadTime(time, nonPriorityRoute));

            nonPriorityRouteSchedule.getScheduleEntries().add(new ScheduleEntry(startTime, nonPriorityRouteSchedule, nonPriorityRoute.getStops().get(0)));
            BN = priorityNnext - BN;

            for(int i = 1 ; i<nonPriorityRoute.getStops().size()-1; i++){
                startTime = startTime.plusMinutes(routeService.getAverageStopToStopTime(time, nonPriorityRoute, nonPriorityRoute.getStops().get(i-1), nonPriorityRoute.getStops().get(i)));
                nonPriorityRouteSchedule
                        .getScheduleEntries()
                        .add(new ScheduleEntry(startTime, nonPriorityRouteSchedule, nonPriorityRoute.getStops().get(i)));
            }

        }
    }

//    @PostConstruct
//    private void init() {
//
//        var routes = routeRepository.findAll();
//
//        var busesWithoutRoute = new ArrayList<>(busRepository.findAll()
//                .stream()
//                .filter(bus -> bus.getRoute() == null)
//                .toList());
//
//        var busesWithRoute = busRepository.findAll()
//                .stream()
//                .filter(bus -> bus.getRoute() != null)
//                .toList();
//
//
//        for (Route route :
//                routes) {
//            routeQueueList.add(new RouteQueue(route));
//        }
//
//        for (RouteQueue routeQueue :
//                routeQueueList) {
//            var route = routeQueue.getRoute();
//            var busesFromRoute = busesWithRoute
//                    .stream()
//                    .filter(bus -> bus.getRoute().equals(route))
//                    .toList();
//
//            for (Bus bus : busesFromRoute) {
//                routeQueue.getBusQueue().add(bus);
//            }
//
//            while (routeQueue.getBusQueue().size() < route.getNorm() && !busesWithoutRoute.isEmpty()) {
//                var bus = busesWithoutRoute.get(0);
//                routeQueue.getBusQueue().add(bus);
//                busesWithoutRoute.remove(bus);
//                bus.setRoute(route);
//            }
//        }
//       //createSchedule();
//    }
}
