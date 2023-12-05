package org.hackathon.buss.service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.hackathon.buss.enums.BusStatus;
import org.hackathon.buss.model.*;
import org.hackathon.buss.repository.ScheduleEntryReposirory;
import org.hackathon.buss.repository.ScheduleRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.hackathon.buss.util.Constants.*;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final RouteService routeService;
    private final BusService busService;
    private List<ScheduleConstructor> scheduleConstructors;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleEntryReposirory scheduleEntryReposirory;
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

    @PostConstruct
    private void init(){
        scheduleConstructors = new ArrayList<>();
        var routes = routeService.findAll();
        var skipList = new ArrayList<Route>();
        for (Route route:
             routes) {
            if(!skipList.contains(route)) {
                var oppositeRoute = routeService.findById(route.getOppositeRouteId()).get();
                skipList.add(oppositeRoute);
                var sc = new ScheduleConstructor(route, oppositeRoute);
                for(Bus bus: busService.findByRoute(route)) {
                    sc.getA_restQueue().add(bus);
                }
                for(Bus bus: busService.findByRoute(oppositeRoute)) {
                    sc.getB_restQueue().add(bus);
                }
                scheduleConstructors.add(
                        sc
                );

            }
        }
    }
    private void updateInfo(){

    }

   private void sendBus(Route route, Queue<LocalDateTime> requestQueue, Queue<Bus> restQueue, Queue<RoadEntry> roadQueue, LocalDateTime time) {
       Schedule schedule = null;

       if (!requestQueue.isEmpty()) {
           var request = requestQueue.peek();
           if (request.getMinute() + request.getHour() * 60 <= time.getHour() * 60 + time.getMinute()) {
               Queue<Bus> returnQueue = new LinkedList<Bus>();
               while (!restQueue.isEmpty()) {
                   var bus = restQueue.poll();
                   if (bus.getStatus() == BusStatus.READY) {
                       requestQueue.poll();
                       schedule = new Schedule();
                       schedule.setStartTime(time);
                       schedule.setRoute(route);
                       schedule.setBus(bus);
                       schedule.setEndTime(time.plusMinutes(routeService.getFullTime(route,
                               LocalDateTime.now().getDayOfWeek().getValue(),
                               getTimeIntervalByDate(time))));
                       route.getSchedules().add(schedule);
                       roadQueue.add(
                               new RoadEntry(
                                       bus,
                                       schedule.getEndTime()
                               )
                       );
                       break;
                   } else
                       returnQueue.add(bus);
               }
               while (!returnQueue.isEmpty()) {
                   restQueue.add(returnQueue.poll());
               }
           }
       }
   }


    //    0 - static
    //    1 - based on statistic
    public void createStatsSchedule(int type){
        List<Route> routes = routeService.findAll();
        List<ScheduleConstructor> scheduleConstructorList = new ArrayList<>();
        for (int i = 0; i < routes.size(); i++) {
            routes.remove(routeService.findById(routes.get(i).getOppositeRouteId()).get());
        }
        for (Route route :
                routes) {
            scheduleConstructorList.add(new ScheduleConstructor(route, routeService.findById(route.getOppositeRouteId()).get()));
        }

        var dayOfWeek = getCurrentDayOfWeek();

        for (ScheduleConstructor sc :
                scheduleConstructorList) {
            var time = sc.getStart();

            while (time.getHour() * 60 + time.getMinute() < sc.getEnd().getHour() * 60 + sc.getEnd().getMinute()) {

                if (time.getMinute() % INTERVAL == 0) {
                    var Anorm = 0;
                    var Bnorm = 0;
                    var Astep = 0;
                    var Bstep = 0;
                    switch (type) {
                        case 0:
                            Anorm = INTERVAL / sc.getA().getNormalStep();
                            Bnorm = INTERVAL / sc.getB().getNormalStep();

                            Astep = sc.getA().getNormalStep();
                            Bstep = sc.getB().getNormalStep();
                            break;
                        case 1:
                            Anorm = routeService.getNorm(sc.getA(), dayOfWeek, getTimeIntervalByDate(time));
                            Bnorm = routeService.getNorm(sc.getB(), dayOfWeek, getTimeIntervalByDate(time));

                            Anorm = Math.max(Anorm, INTERVAL / sc.getA().getNormalStep());
                            Bnorm = Math.max(Bnorm, INTERVAL / sc.getB().getNormalStep());

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

                for (Bus bus :
                        sc.getA_restQueue()) {
                    if (bus.getStatus() == BusStatus.CHARGING) {
                        if (bus.getCharge() <= 80)
                            bus.setCharge(bus.getCharge() + 100.0 / FULL_CHARGE_TIME);

                        if (bus.getCharge() > 80)
                            bus.setStatus(BusStatus.READY);
                    }
                }

                for (Bus bus :
                        sc.getB_restQueue()) {
                    if (bus.getStatus() == BusStatus.CHARGING) {
                        if (bus.getCharge() <= 80)
                            bus.setCharge(bus.getCharge() + 100.0 / FULL_CHARGE_TIME);

                        if (bus.getCharge() > 80)
                            bus.setStatus(BusStatus.READY);
                    }
                }

                var aPeek = sc.getA_roadQueue().peek();
                if (aPeek != null) {
                    if (time.getMinute() + time.getHour() * 60 >= aPeek.getArrivalTime().getHour() * 60 + aPeek.getArrivalTime().getMinute()) {
                        var bus = sc.getA_roadQueue().poll().getBus();

                        bus.setCharge(bus.getCharge() - routeService.getFullDistance(sc.getA()) / FULL_CHARGE_DISTANCE * 100);

                        if (bus.getCharge() <= 20.0)
                            bus.setStatus(BusStatus.CHARGING);
                        sc.getB_restQueue().add(bus);
                    }
                }

                var bPeek = sc.getB_roadQueue().peek();
                if (bPeek != null) {
                    if (time.getMinute() + time.getHour() * 60 >= bPeek.getArrivalTime().getHour() * 60 + bPeek.getArrivalTime().getMinute()) {
                        var bus = sc.getB_roadQueue().poll().getBus();

                        bus.setCharge(bus.getCharge() - routeService.getFullDistance(sc.getB()) / FULL_CHARGE_DISTANCE * 100);

                        if (bus.getCharge() <= 20.0)
                            bus.setStatus(BusStatus.CHARGING);
                        sc.getA_restQueue().add(bus);
                    }
                }

                sendBus(sc.getA(),
                        sc.getA_requestQueue(),
                        sc.getA_restQueue(),
                        sc.getA_roadQueue(),
                        time);

               sendBus(sc.getB(),
                       sc.getB_requestQueue(),
                       sc.getB_restQueue(),
                       sc.getB_roadQueue(),
                       time);

                time = time.plusMinutes(1);
            }
            routeService.save(sc.getA(), sc.getB());
        }
        for (Bus bus:
                busService.findAll()) {
            bus.setSchedule(null);
            busService.save(bus);
        }
    }

}