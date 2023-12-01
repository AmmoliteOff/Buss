package org.hackathon.buss.service;

import lombok.AllArgsConstructor;
import org.hackathon.buss.enums.BusStatus;
import org.hackathon.buss.model.*;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static org.hackathon.buss.util.Constants.*;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final RouteService routeService;
    private int getCurrentTimeIntervalInt() {
        var time = LocalDateTime.now();
        int minute = time.getMinute();
        int roundedMinute = minute - minute % 30;

        var c = time.withMinute(roundedMinute).withSecond(0).withNano(0);
        var minutes = c.getHour()*60+c.getMinute();
        return minutes/ INTERVAL;
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

    public void createStaticSchedule(){
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

        }
    }

//    0 - static
//    1 - based on statistic
//    2- real time + statistic
    public void createStatsSchedule(int type){
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

                if(time.getMinute()%INTERVAL == 0) {
                    var Anorm = 0;
                    var Bnorm = 0;
                    var Astep = 0;
                    var Bstep = 0;
                    switch (type) {
                        case 0:
                            Anorm = INTERVAL/sc.getA().getStandartStep();
                            Bnorm = INTERVAL/sc.getB().getStandartStep();

                            Astep = sc.getA().getStandartStep();
                            Bstep = sc.getB().getStandartStep();
                        break;
                        case 1:
                            Anorm = routeService.getNorm(sc.getA(), dayOfWeek, getTimeIntervalByDate(time));
                            Bnorm = routeService.getNorm(sc.getB(), dayOfWeek, getTimeIntervalByDate(time));

                            Anorm = Math.max(Anorm, INTERVAL / sc.getA().getStandartStep());
                            Bnorm = Math.max(Bnorm, INTERVAL / sc.getB().getStandartStep());

                            Astep = INTERVAL / Anorm;
                            Bstep = INTERVAL / Bnorm;
                            break;
                        case 2:
                            break;
                    }
                    for (int i = 0; i < Anorm; i++) {
                        var requestTime = time.plusMinutes((long) Astep * i);
                        sc.getA_requestQueue().add(requestTime);
                    }

                    for (int j = 0; j < Bnorm; j++) {
                        var requestTime = time.plusMinutes((long) Bstep * j);
                        sc.getB_requestQueue().add(requestTime);
                    }
                }

                for (Bus bus:
                        sc.getA_restQueue()) {
                    if(bus.getStatus() == BusStatus.CHARGING){
                        if(bus.getCharge() <= 80)
                            bus.setCharge(bus.getCharge()+ 100.0/FULL_CHARGE_TIME);

                        if(bus.getCharge() > 80)
                            bus.setStatus(BusStatus.READY);
                    }
                }

                for (Bus bus:
                        sc.getB_restQueue()) {
                    if(bus.getStatus() == BusStatus.CHARGING){
                        if(bus.getCharge() <= 80)
                            bus.setCharge(bus.getCharge()+ 100.0/FULL_CHARGE_TIME);

                        if(bus.getCharge() > 80)
                            bus.setStatus(BusStatus.READY);
                    }
                }

                var aPeek = sc.getA_roadQueue().peek();
                if(aPeek!=null) {
                    if (time.getMinute() + time.getHour() * 60 >= aPeek.getArrivalTime().getHour() * 60 + aPeek.getArrivalTime().getMinute()) {
                        var bus = sc.getA_roadQueue().poll().getBus();

                        bus.setCharge(bus.getCharge() - routeService.getFullDistance(sc.getA())/FULL_CHARGE_DISTANCE * 100);

                        if(bus.getCharge() <= 20.0)
                            bus.setStatus(BusStatus.CHARGING);
                        sc.getB_restQueue().add(bus);
                    }
                }

                var bPeek = sc.getB_roadQueue().peek();
                if(bPeek!=null) {
                    if (time.getMinute() + time.getHour() * 60 >= bPeek.getArrivalTime().getHour() * 60 + bPeek.getArrivalTime().getMinute()) {
                        var bus = sc.getB_roadQueue().poll().getBus();

                        bus.setCharge(bus.getCharge() - routeService.getFullDistance(sc.getB())/FULL_CHARGE_DISTANCE * 100);

                        if(bus.getCharge() <= 20.0)
                            bus.setStatus(BusStatus.CHARGING);
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
        }
    }
}