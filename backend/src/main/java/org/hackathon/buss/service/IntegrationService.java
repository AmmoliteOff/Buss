package org.hackathon.buss.service;

import org.hackathon.buss.model.Stop;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class IntegrationService {
    public int getPeopleCount(Stop stop){
        Random random = new Random();
        return random.nextInt(1, 50);
    }
}
