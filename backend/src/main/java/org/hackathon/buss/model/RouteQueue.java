package org.hackathon.buss.model;

import lombok.Data;

import java.util.*;

@Data
public class RouteQueue{
    Queue<Bus> busQueue = new ArrayDeque<>();
    private Route route;
    public RouteQueue(Route route){
        this.route = route;
    }
}
