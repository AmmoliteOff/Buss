package org.hackathon.buss.controller;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.model.RouteChange;
import org.hackathon.buss.service.RouteChangeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/route-changes")
public class RouteChangeController {

    private final RouteChangeService routeChangeService;

    @GetMapping("/{id}")
    public ResponseEntity<RouteChange> getRouteChange(@PathVariable Long id) {
        RouteChange routeChange = routeChangeService.findById(id).orElse(null);
        return ResponseEntity.ok(routeChange);
    }

    @GetMapping("")
    public ResponseEntity<List<RouteChange>> getAllRouteChanges() {
        List<RouteChange> routeChanges = routeChangeService.findAll();
        return ResponseEntity.ok(routeChanges);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RouteChange> deleteRouteChange(@PathVariable Long id) {
        routeChangeService.delete(id);
        return ResponseEntity.ok(new RouteChange());
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteAllRouteChanges() {
        routeChangeService.deleteAll();
        return ResponseEntity.ok("Success");
    }
}
