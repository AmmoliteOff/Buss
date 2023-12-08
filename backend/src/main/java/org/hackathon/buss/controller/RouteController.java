package org.hackathon.buss.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.hackathon.buss.dto.BusDTO;
import org.hackathon.buss.dto.RouteDTO;
import org.hackathon.buss.model.Route;
import org.hackathon.buss.service.BusService;
import org.hackathon.buss.service.RouteService;
import org.hackathon.buss.service.ScheduleService;
import org.hackathon.buss.util.view.NonDetailedInformation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;
    private final ScheduleService scheduleService;
    private final BusService busService;

    @GetMapping("/{id}")
    @JsonView(NonDetailedInformation.class)
    public ResponseEntity<Route> getRoute(@PathVariable Long id) {
        Route route = routeService.findById(id).orElse(null);
        return ResponseEntity.ok(route);
    }

    @GetMapping("")
    @JsonView(NonDetailedInformation.class)
    public ResponseEntity<List<Route>> getAllRoutes() {
        List<Route> routes = routeService.findAll();
        return ResponseEntity.ok(routes);
    }

    @PostMapping()
    @JsonView(NonDetailedInformation.class)
    public  ResponseEntity<List<Route>> addRoute(@RequestBody RouteDTO routeDTO) {
        List<Route> savedRoute = routeService.save(routeDTO.getRoute(), routeDTO.getOppositeRoute());
        return ResponseEntity.ok(savedRoute);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoute(@PathVariable Long id) {
        routeService.delete(id);
        return ResponseEntity.ok("OK");
    }


    @PostMapping("/createSchedule/{type}")
    public ResponseEntity<String> createSchedule(@PathVariable int type){
        scheduleService.createStatsSchedule(type);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/finish")
    public ResponseEntity<String> routeEnd(@RequestBody BusDTO busDto){
        //scheduleService.busReachedEnd(busService.findById(busDto.getBusId()).get());
        return ResponseEntity.ok("OK");
    }
}
