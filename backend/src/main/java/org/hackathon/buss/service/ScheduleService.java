package org.hackathon.buss.service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.hackathon.buss.model.*;
import org.hackathon.buss.repository.BusRepository;
import org.hackathon.buss.repository.RouteRepository;
import org.hackathon.buss.repository.ScheduleEntryReposirory;
import org.hackathon.buss.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final int interval = 30;
    private final BusRepository busRepository;
    private final IntegrationService integrationService;
    private final RouteService routeService;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleEntryReposirory scheduleEntryRepository;
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

    private int getTimeIntervalByDate(LocalDateTime dateTime){
        LocalTime midnight = LocalTime.MIDNIGHT;
        long minutesSinceMidnight = ChronoUnit.MINUTES.between(midnight, dateTime.toLocalTime());

//        if (timeSlot == 48 && dateTime.getMinute() > 0) {
//            timeSlot = 1;
//        }

        return (int) (minutesSinceMidnight / 30) + 1;
    }

    private int getCurrentDayOfWeek(){
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        return dayOfWeek.getValue();
    }
    private void updateBusStationsLoadInfo() {
        //
    }
    public void createScheduleBasedOnStats() {
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

        var dayOfWeek = getCurrentDayOfWeek();
        for(ScheduleConstructor sc: scheduleConstructorList) {
            var busesA = busRepository.findAllByRoute(sc.getA());
            var busesB = busRepository.findAllByRoute(sc.getB());

            for (Bus bus :
                    busesA) {
                sc.getARouteBusQueue().add(bus);
            }

            for (Bus bus :
                    busesB) {
                sc.getBRouteBusQueue().add(bus);
            }

            var time = getTimeIntervalByDate(sc.getStart());
            var currentTimeInterval = LocalDateTime.now()
                    .withHour(sc.getStart().getHour())
                    .withMinute(sc.getStart().getMinute())
                    .withSecond(sc.getStart().getSecond())
                    .withNano(sc.getStart().getNano());
            var priorityStartTime = sc.getStart();
            while (currentTimeInterval.getHour() * 60 + currentTimeInterval.getMinute() < sc.getEnd().getHour() * 60 + sc.getEnd().getMinute()) {
                var AN = routeService.getNorm(sc.getA(), dayOfWeek, time);
                var BN = routeService.getNorm(sc.getB(), dayOfWeek, time);

                var priorityRoute = AN > BN ? sc.getA() : sc.getB();
                var nonPriorityRoute = AN > BN ? sc.getB() : sc.getA();

                var priorityNnext = routeService.getNorm(priorityRoute, dayOfWeek, time + 1);

                var priorityIntervalEnd = currentTimeInterval.plusMinutes(interval);

                var nonPriorityStartTime = (priorityIntervalEnd.plusMinutes(interval / priorityNnext))
                        .minusMinutes(routeService.getAverageRoadTime(time, nonPriorityRoute));

                int priorityStep = 0;
                int nonPriorityStep = 0;
                int nonPriorityNorm = 0;
                int priorityNorm = 0;


                if (priorityRoute == sc.getA()) {
                    int val = priorityNnext - BN;
                    nonPriorityNorm = BN + val;
                    priorityNorm = AN;
                } else {
                    int val = priorityNnext - AN;
                    nonPriorityNorm = AN + val;
                    priorityNorm = BN;
                }

                if (priorityNorm > sc.getRouteBusQueue(priorityRoute).size() + sc.getRouteRestBusQueue(priorityRoute).size())
                    priorityNorm = sc.getRouteBusQueue(priorityRoute).size() + sc.getRouteRestBusQueue(priorityRoute).size();
                if (nonPriorityNorm > sc.getRouteBusQueue(nonPriorityRoute).size() + sc.getRouteRestBusQueue(nonPriorityRoute).size())
                    nonPriorityNorm = sc.getRouteBusQueue(nonPriorityRoute).size() + sc.getRouteRestBusQueue(nonPriorityRoute).size();

                while (priorityNorm > sc.getRouteBusQueue(priorityRoute).size() && !sc.getRouteRestBusQueue(priorityRoute).isEmpty()) {
                    sc.getRouteBusQueue(priorityRoute).add(sc.getRouteRestBusQueue(priorityRoute).poll());
                }

                while (nonPriorityNorm > sc.getRouteBusQueue(nonPriorityRoute).size() && !sc.getRouteRestBusQueue(nonPriorityRoute).isEmpty()) {
                    sc.getRouteBusQueue(nonPriorityRoute).add(sc.getRouteRestBusQueue(nonPriorityRoute).poll());
                }

                while (priorityNorm < sc.getRouteBusQueue(priorityRoute).size() && !sc.getRouteBusQueue(priorityRoute).isEmpty()) {
                    sc.getRouteRestBusQueue(priorityRoute).add(sc.getRouteBusQueue(priorityRoute).poll());
                }

                while (nonPriorityNorm < sc.getRouteBusQueue(nonPriorityRoute).size() && !sc.getRouteBusQueue(nonPriorityRoute).isEmpty()) {
                    sc.getRouteRestBusQueue(nonPriorityRoute).add(sc.getRouteBusQueue(nonPriorityRoute).poll());
                }

                priorityStep = interval / priorityNorm;
                nonPriorityStep = interval / nonPriorityNorm;

                for (int i = 0; i < priorityNorm; i++) {
                    var scheduleStartTime = priorityStartTime.plusMinutes((long) priorityStep * i);
                    var schedule = createSchedule(priorityRoute, scheduleStartTime, time);
                    var bus = sc.getRouteBusQueue(priorityRoute).poll();
                    schedule.setBus(bus);
                    scheduleRepository.save(schedule);
                    priorityRoute.getSchedules().add(schedule);
                    sc.getRouteBusInRoad(priorityRoute).add(bus);
                }

                for (int i = 0; i < nonPriorityNorm; i++) {
                    var scheduleStartTime = nonPriorityStartTime.plusMinutes((long) nonPriorityStep * i);
                    var schedule = createSchedule(nonPriorityRoute, scheduleStartTime, time);
                    var bus = sc.getRouteBusQueue(nonPriorityRoute).poll();
                    schedule.setBus(bus);
                    scheduleRepository.save(schedule);
                    nonPriorityRoute.getSchedules().add(schedule);
                    sc.getRouteBusInRoad(nonPriorityRoute).add(bus);
                }

                time++;
                currentTimeInterval = currentTimeInterval.plusMinutes(interval);
                priorityStartTime = priorityStartTime.plusMinutes(routeService.getAverageRoadTime(time, nonPriorityRoute));
                for (int i = 0; i < sc.getRouteBusInRoad(nonPriorityRoute).size(); i++) {
                    sc.getRouteBusQueue(priorityRoute).add(sc.getRouteBusInRoad(nonPriorityRoute).poll());
                }

                for (int i = 0; i < sc.getRouteBusInRoad(priorityRoute).size(); i++) {
                    sc.getRouteBusQueue(nonPriorityRoute).add(sc.getRouteBusInRoad(priorityRoute).poll());
                }
            }
        }
        routeRepository.saveAll(routes);
    }

    private Schedule createSchedule(Route route, LocalDateTime from, int time){
        from = from.plusMinutes(0);
        var schedule = new Schedule();
        for(int i = 0; i<route.getStops().size(); i++){
            var se = new ScheduleEntry(from, schedule, route.getStops().get(i));
            schedule
                    .getScheduleEntries()
                    .add(se);
            scheduleEntryRepository.save(se);
            if(i+1<route.getStops().size())
                from = from.plusMinutes(routeService.getAverageStopToStopTime(time, route, route.getStops().get(i), route.getStops().get(i+1)));
        }
        return schedule;
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
