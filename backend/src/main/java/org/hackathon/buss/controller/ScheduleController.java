package org.hackathon.buss.controller;

import lombok.AllArgsConstructor;
import org.hackathon.buss.model.Schedule;
import org.hackathon.buss.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("")
    public ResponseEntity<List<Schedule>> getAllSchedules(){
        return ResponseEntity.ok(scheduleService.findAllSchedules());
    }
}
