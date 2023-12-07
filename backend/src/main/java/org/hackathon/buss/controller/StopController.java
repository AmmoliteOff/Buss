package org.hackathon.buss.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.hackathon.buss.dto.BusDTO;
import org.hackathon.buss.dto.PosDTO;
import org.hackathon.buss.dto.StopDTO;
import org.hackathon.buss.model.Stop;
import org.hackathon.buss.service.*;
import org.hackathon.buss.util.view.NonDetailedInformation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stops")
public class StopController {

    private final StopService stopService;
    private final BusService busService;

    @GetMapping("/{id}")
    public ResponseEntity<Stop> getStop(@PathVariable Long id) {
        Stop stop = stopService.findById(id).orElse(null);
        return ResponseEntity.ok(stop);
    }

    @GetMapping("/busesNear")
    public ResponseEntity<List<BusDTO>> findNearBus(@RequestBody PosDTO posDTO){
        var stop = stopService.findByPos(posDTO);
        return ResponseEntity.ok(busService.getNearBuses(stop));
    }

    @GetMapping("")
    @JsonView(NonDetailedInformation.class)
    public ResponseEntity<List<Stop>> getAllStops() {
        List<Stop> stops = stopService.findAll();
        return ResponseEntity.ok(stops);
    }

    @GetMapping("/{stopId}/{routeId}")
    @JsonView(NonDetailedInformation.class)
    public ResponseEntity<StopDTO> getStop(@PathVariable("stopId") String stopId, @PathVariable("routeId") String routeId ) {
        StopDTO stopDTO = stopService.getInfo(Long.parseLong(stopId), Long.parseLong(routeId));
        return ResponseEntity.ok(stopDTO);

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
