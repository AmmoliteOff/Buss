package org.hackathon.buss.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.hackathon.buss.dto.BusDTO;
import org.hackathon.buss.model.Bus;
import org.hackathon.buss.service.BusService;
import org.hackathon.buss.util.view.NonDetailedInformation;
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

    @PatchMapping("/update")
    @JsonView(NonDetailedInformation.class)
    public  ResponseEntity<Bus> updateBus(@RequestBody BusDTO bus) {
        return ResponseEntity.ok(busService.update(bus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Bus> deleteBus(@PathVariable Long id) {
        busService.delete(id);
        return ResponseEntity.ok(new Bus());
    }
}
