package org.hackathon.buss.controller;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.model.Bus;
import org.hackathon.buss.service.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/driver")
public class DriverController {

    private final StatsService statsService;

    @PostMapping("/nextCheckPoint")
    public ResponseEntity<HttpStatus> nextCheckPoint(Bus bus){
        if(bus.getBusDriver()!=null && bus.getSchedule()!=null) {
            statsService.updateNextStationInfo(bus);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
