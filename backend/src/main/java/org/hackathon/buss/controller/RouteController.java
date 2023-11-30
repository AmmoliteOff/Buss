package org.hackathon.buss.controller;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.dto.RouteChangeDTO;
import org.hackathon.buss.model.Route;
import org.hackathon.buss.model.Schedule;
import org.hackathon.buss.service.RouteService;
import org.hackathon.buss.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;
    private final ScheduleService scheduleService;

    @GetMapping("/{id}")
    public ResponseEntity<Route> getRoute(@PathVariable Long id) {
        Route route = routeService.findById(id).orElse(null);
        return ResponseEntity.ok(route);
    }

    @GetMapping("")
    public ResponseEntity<List<Route>> getAllRoutes() {
        List<Route> routes = routeService.findAll();
        return ResponseEntity.ok(routes);
    }

    @PostMapping()
    public  ResponseEntity<Route> addRoute(@RequestBody Route route) {
        return ResponseEntity.ok(routeService.save(route));
    }

    @PatchMapping("/{id}")
    public  ResponseEntity<Route> updateRoute(@PathVariable Long id, @RequestBody RouteChangeDTO routeChangeDTO) {
        return ResponseEntity.ok(routeService.update(id, routeChangeDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Route> deleteRoute(@PathVariable Long id) {
        routeService.delete(id);
        return ResponseEntity.ok(new Route());
    }


    @PostMapping("/lol")
    public ResponseEntity<List<Route>> dasd(){
        scheduleService.createStatsSchedule(0);
        return new ResponseEntity<List<Route>>(routeService.findAll(), HttpStatus.OK);
    }
}
