package org.hackathon.buss.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Queue;

@Data
public class ScheduleConstructor {
    private Route A;
    private Route B;

    private Queue<Bus> ARouteBusQueue = new ArrayDeque<>();
    private Queue<Bus> BRouteBusQueue = new ArrayDeque<>();

    private Queue<Bus> ARouteBusRestQueue = new ArrayDeque<>();
    private Queue<Bus> BRouteBusRestQueue = new ArrayDeque<>();

    private Queue<Bus> ARouteBusInRoad = new ArrayDeque<>();
    private Queue<Bus> BRouteBusInRoad= new ArrayDeque<>();

    private LocalDateTime start;
    private LocalDateTime end;

    public ScheduleConstructor(Route A, Route B){
        this.A = A;
        this.B = B;
        start = A.getStartTime();
        end = A.getEndTime();
    }

    public Queue<Bus> getRouteBusQueue(Route route){
        if(route.equals(A))
            return ARouteBusQueue;
        else
            return BRouteBusQueue;
    }

    public Queue<Bus> getRouteBusInRoad(Route route){
        if(route.equals(A))
            return ARouteBusInRoad;
        else
            return BRouteBusInRoad;
    }

    public Queue<Bus> getRouteRestBusQueue(Route route){
        if(route.equals(A))
            return ARouteBusRestQueue;
        else
            return BRouteBusRestQueue;
    }
}
