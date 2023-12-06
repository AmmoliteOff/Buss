package org.hackathon.buss.service;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.dto.PosDTO;
import org.hackathon.buss.model.Stop;
import org.hackathon.buss.model.Waypoint;
import org.hackathon.buss.repository.StopRepository;
import org.hackathon.buss.util.Constants;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class StopService {

    private final StopRepository stopRepository;

    public Optional<Stop> findById(long id) {
        return stopRepository.findById(id);
    }

    public List<Stop> findAll() {
        return stopRepository.findAll();
    }

    public Stop save(Stop stop) {
        return stopRepository.save(stop);
    }

    public void delete(Long id) {
        stopRepository.delete(findById(id).orElseThrow());
    }

    public Stop findByPos(PosDTO posDTO){
        Stop closestStop = null;
        var stops = stopRepository.findAll();
        var minDistance = 100000.0;
        for(Stop stop: stops){
            var wa = new Waypoint();
            var wb = new Waypoint();
            wa.setLatitude(posDTO.getLatitude());
            wa.setLongitude(posDTO.getLongitude());
            wb.setLongitude(stop.getLongitude());
            wb.setLatitude(stop.getLatitude());
            var distance = DistanceService.calculateDistance(wa, wb);
                if(distance<minDistance){
                    minDistance = distance;
                    closestStop = stop;
                }
            }
        return closestStop;
    }
    public Stop update(Long id, Stop newStop) {
        newStop.setId(id);
        return save(newStop);
    }

    public Optional<Stop> findByTitle (String title) {
        return stopRepository.findByTitle(title);
    }
}
