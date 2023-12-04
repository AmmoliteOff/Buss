package org.hackathon.buss.controller;


import lombok.RequiredArgsConstructor;
import org.hackathon.buss.model.CarAccident;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/car-accidents")
public class CarAccidentController {


    @PostMapping()
    public ResponseEntity<List<CarAccident>> acceptCarAccidents(@RequestBody List<CarAccident> carAccidents) {
        return ResponseEntity.ok(carAccidents);
    }
}
