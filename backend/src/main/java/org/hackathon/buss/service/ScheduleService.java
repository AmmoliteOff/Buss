package org.hackathon.buss.service;

import lombok.AllArgsConstructor;
import org.hackathon.buss.repository.BusRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final BusRepository busRepository;
    //@Scheduled()
    public void calculateSchedule(){

    }
}
