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

    private LocalDateTime start;
    private LocalDateTime end;

    public ScheduleConstructor(Route A, Route B){
        this.A = A;
        this.B = B;
    }
}
