package org.hackathon.buss.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Queue;

@Data
public class ScheduleConstructor {
    private Route A;
    private Route B;

    private LocalDateTime start;
    private LocalDateTime end;

    private int sentA = 0;
    private int sentB = 0;

    private LocalDateTime lastSentA;
    private LocalDateTime lastSentB;

    private Queue<LocalDateTime> A_requestQueue = new ArrayDeque<LocalDateTime>();
    private Queue<LocalDateTime> B_requestQueue = new ArrayDeque<LocalDateTime>();

    private Queue<RoadEntry> A_roadQueue = new ArrayDeque<RoadEntry>();
    private Queue<RoadEntry> B_roadQueue = new ArrayDeque<RoadEntry>();

    private Queue<Bus> A_restQueue = new ArrayDeque<Bus>();
    private Queue<Bus> B_restQueue = new ArrayDeque<Bus>();

    public ScheduleConstructor(Route A, Route B){
        this.A = A;
        this.B = B;
        start = A.getStartTime();
        end = A.getEndTime();
        A_restQueue.addAll(A.getBuses());
        B_restQueue.addAll(B.getBuses());
    }

}
