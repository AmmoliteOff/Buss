package org.hackathon.buss.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.hackathon.buss.dto.BusDTO;
import org.hackathon.buss.dto.RouteChangeDTO;
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

    //@GetMapping("/{id}")
    //@JsonView(NonDetailedInformation.class)
//    public ResponseEntity<RouteDTO> getRoute(@PathVariable Long id) {
//        Route route = routeService.findById(id).orElse(null);
//        RouteDTO routeDTO = RouteDTO.builder()
//                .route(route)
//                .oppositeRouteId(route.getOppositeRoute().getId())
//                .build();
//        return ResponseEntity.ok(routeDTO);
//    }

    @GetMapping("")
    public ResponseEntity<List<Route>> getAllRoutes() {
        List<Route> routes = routeService.findAll();
        return ResponseEntity.ok(routes);
    }

    @PostMapping()
    @JsonView(NonDetailedInformation.class)
    public  ResponseEntity<List<Route>> addRoute(@RequestBody RouteDTO routeDTO) {
        List<Route> savedRoute = routeService.save(routeDTO.getRoute(), routeDTO.getOppositeRoute());
//        RouteDTO routeDTO = RouteDTO.builder()
//                .route(savedRoute)
//                .oppositeRouteId(savedRoute.getOppositeRoute().getId())
//                .build();
        return ResponseEntity.ok(savedRoute); ///
    }

    @PatchMapping("/{id}")
    @JsonView(NonDetailedInformation.class)
    public  ResponseEntity<RouteDTO> updateRoute(@PathVariable Long id, @RequestBody RouteChangeDTO routeChangeDTO) {
       Route route = routeService.update(id, routeChangeDTO);
        //DTO
        return ResponseEntity.ok(null);
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
