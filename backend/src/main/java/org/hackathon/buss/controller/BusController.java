package org.hackathon.buss.controller;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.model.Bus;
import org.hackathon.buss.service.BusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/buses")
public class BusController {

    private final BusService busService;

    @GetMapping("/{id}")
    public ResponseEntity<Bus> getBus(@PathVariable Long id) {
        Bus bus = busService.findById(id).orElse(null);
        return ResponseEntity.ok(bus);
    }

    @GetMapping("")
    public ResponseEntity<List<Bus>> getAllBuses() {
        List<Bus> buses = busService.findAll();
        return ResponseEntity.ok(buses);
    }

    @PostMapping()
    public  ResponseEntity<Bus> addBus(@RequestBody Bus bus) {
        return ResponseEntity.ok(busService.save(bus));
    }

    @PatchMapping("/{id}")
    public  ResponseEntity<Bus> updateBus(@PathVariable Long id, @RequestBody Bus bus) {
        return ResponseEntity.ok(busService.update(id, bus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Bus> deleteBus(@PathVariable Long id) {
        busService.delete(id);
        return ResponseEntity.ok(new Bus());
    }
}
