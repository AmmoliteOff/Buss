package org.hackathon.buss.controller;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.model.Stop;
import org.hackathon.buss.service.StopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stops")
public class StopController {

    private final StopService stopService;

    @GetMapping("/{id}")
    public ResponseEntity<Stop> getStop(@PathVariable Long id) {
        Stop stop = stopService.findById(id).orElse(null);
        return ResponseEntity.ok(stop);
    }

    @GetMapping("")
    public ResponseEntity<List<Stop>> getAllStops() {
        List<Stop> stops = stopService.findAll();
        return ResponseEntity.ok(stops);
    }

    @PostMapping()
    public  ResponseEntity<Stop> addStop(@RequestBody Stop stop) {
        return ResponseEntity.ok(stopService.save(stop));
    }

    @PatchMapping("/{id}")
    public  ResponseEntity<Stop> updateRoute(@PathVariable Long id, @RequestBody Stop stop) {
        return ResponseEntity.ok(stopService.update(id, stop));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Stop> deleteStop(@PathVariable Long id) {
        stopService.delete(id);
        return ResponseEntity.ok(new Stop());
    }
}