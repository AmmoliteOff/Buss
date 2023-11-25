package org.hackathon.buss.controller;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.model.Route;
import org.hackathon.buss.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    @GetMapping("/{id}")
    public ResponseEntity<Route> getBus(@PathVariable Long id) {
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
    public  ResponseEntity<Route> updateRoute(@PathVariable Long id, @RequestBody Route route) {
        return ResponseEntity.ok(routeService.update(id, route));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Route> deleteRoute(@PathVariable Long id) {
        routeService.delete(id);
        return ResponseEntity.ok(new Route());
    }

}
