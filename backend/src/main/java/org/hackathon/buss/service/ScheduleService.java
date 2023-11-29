package org.hackathon.buss.service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.hackathon.buss.enums.BusStatus;
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
import java.util.Queue;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final int interval = 30;
    private final RouteService routeService;
    private final BusService busService;
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
        return (int) (minutesSinceMidnight / 30) + 1;
    }

    private int getCurrentDayOfWeek(){
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        return dayOfWeek.getValue();
    }

    private Schedule createSchedule(Queue<LocalDateTime> queue, LocalDateTime time, Route route){
        Schedule schedule = null;
        if(!queue.isEmpty()) {
            var request = queue.peek();
            if (request.getHour() * 60 + request.getMinute() <= time.getHour() * 60 + time.getMinute()) {
                Stop prevStop = null;
                var arrivalTime = time.plusMinutes(0);
                schedule = new Schedule();
                queue.poll();
                for (int j = 0; j < route.getRoute().size(); j++) {
                    if (route.getRoute().get(j).getStop() != null) {
                        if (prevStop != null) {
                            arrivalTime = arrivalTime.plusMinutes(routeService
                                    .getAverageStopToStopTime(getTimeIntervalByDate(time), route, prevStop, route.getRoute().get(j).getStop()));
                        }
                        schedule.getScheduleEntries().add(
                                new ScheduleEntry(
                                        arrivalTime,
                                        schedule,
                                        route.getRoute().get(j).getStop()
                                )
                        );
                        prevStop = route.getRoute().get(j).getStop();
                    }
                }
                route.getSchedules().add(schedule);
            }
        }
        return schedule;
    }
    public void createStatsSchedule(){
        List<Route> routes = routeService.findAll();
        List<ScheduleConstructor> scheduleConstructorList = new ArrayList<>();
        for(int i = 0; i<routes.size(); i++){
            routes.remove(routes.get(i).getOppositeRoute());
        }
        for (Route route:
             routes) {
            scheduleConstructorList.add(new ScheduleConstructor(route, route.getOppositeRoute()));
        }

        var dayOfWeek = getCurrentDayOfWeek();

        for (ScheduleConstructor sc:
             scheduleConstructorList) {
            var time = sc.getStart();

            while(time.getHour()*60 + time.getMinute() < sc.getEnd().getHour() * 60 + sc.getEnd().getMinute()){

                if(time.getMinute()%interval == 0) {
                    var Anorm = routeService.getNorm(sc.getA(), dayOfWeek, getTimeIntervalByDate(time));
                    var Bnorm = routeService.getNorm(sc.getB(), dayOfWeek, getTimeIntervalByDate(time));

                    Anorm = Math.max(Anorm, interval / sc.getA().getStandartStep());
                    Bnorm = Math.max(Bnorm, interval / sc.getB().getStandartStep());

                    var Astep = interval/Anorm;
                    var Bstep = interval/Bnorm;

                    for(int i = 0; i<Anorm; i++){
                        var requestTime = time.plusMinutes((long) Astep *i);
                        sc.getA_requestQueue().add(requestTime);
                    }

                    for(int j = 0; j<Bnorm; j++){
                        var requestTime = time.plusMinutes((long) Bstep * j);
                        sc.getB_requestQueue().add(requestTime);
                    }
                }
                var aPeek = sc.getA_roadQueue().peek();
                if(aPeek!=null) {
                    if (time.getMinute() + time.getHour() * 60 >= aPeek.getArrivalTime().getHour() * 60 + aPeek.getArrivalTime().getMinute()) {
                        var bus = sc.getA_roadQueue().poll().getBus();
                        sc.getB_restQueue().add(bus);
                        var g = 0;
                    }
                }

                var bPeek = sc.getB_roadQueue().peek();
                if(bPeek!=null) {
                    if (time.getMinute() + time.getHour() * 60 >= bPeek.getArrivalTime().getHour() * 60 + bPeek.getArrivalTime().getMinute()) {
                        var bus = sc.getB_roadQueue().poll().getBus();
                        sc.getA_restQueue().add(bus);
                    }
                }

                int checkAmount = sc.getA_restQueue().size();
                for(int i = 0; i<checkAmount; i++){
                    var bus = sc.getA_restQueue().peek();
                    if(bus.getStatus() == BusStatus.READY) {
                        var schedule = createSchedule(sc.getA_requestQueue(), time, sc.getA());
                        if(schedule!=null) {
                            schedule.setBus(bus);
                            sc.getA_restQueue().poll();
                                sc.getA_roadQueue().add(
                                        new RoadEntry(
                                                bus,
                                                schedule.getScheduleEntries()
                                                        .get(schedule.getScheduleEntries().size() - 1)
                                                        .getTime()
                                        )
                                );
                        }
                    }
                }

                checkAmount = sc.getB_restQueue().size();
                for(int i = 0; i<checkAmount; i++){
                    var bus = sc.getB_restQueue().peek();
                    if(bus.getStatus() == BusStatus.READY){
                        var schedule = createSchedule(sc.getB_requestQueue(), time, sc.getB());
                        if(schedule!=null) {
                            schedule.setBus(bus);
                            sc.getB_restQueue().poll();
                                sc.getB_roadQueue().add(
                                        new RoadEntry(
                                                bus,
                                                schedule.getScheduleEntries()
                                                        .get(schedule.getScheduleEntries().size() - 1)
                                                        .getTime()
                                        )
                                );
                        }
                    }
                }


                time = time.plusMinutes(1);
            }
            var a = 0;
        }

    }
}
