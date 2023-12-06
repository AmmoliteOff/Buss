package org.hackathon.buss.service;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.dto.BusDTO;
import org.hackathon.buss.dto.PosDTO;
import org.hackathon.buss.enums.BusStatus;
import org.hackathon.buss.model.Bus;
import org.hackathon.buss.model.Route;
import org.hackathon.buss.model.Stop;
import org.hackathon.buss.repository.BusRepository;
import org.hackathon.buss.util.Constants;
import org.hackathon.buss.model.RoadStops;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BusService {

    private final BusRepository busRepository;
    private final RouteService routeService;
    //private final ScheduleService scheduleService;

    public Optional<Bus> findById(long id) {
        return busRepository.findById(id);
    }

    public List<Bus> findAll() {
        return busRepository.findAll();
    }

    public Bus save(Bus bus) {
        return busRepository.save(bus);
    }

    public void delete(Long id) {
        busRepository.delete(findById(id).orElseThrow());
    }

    public List<Bus> findByRoute(Route route){
        return busRepository.findAllByRoute(route);
    }
    public Bus update(BusDTO busDTO) {
        var bus = busRepository.findById(busDTO.getBusId()).get();
        bus.setCharge(busDTO.getCharge());
        bus.setLatitude(busDTO.getLatitude());
        bus.setLongitude(busDTO.getLongitude());
        if(busDTO.isCharging())
            bus.setStatus(BusStatus.CHARGING);

        if(bus.getStatus() == BusStatus.IN_ROAD) {
            RoadStops nextStop = null;
            for(int i = 0; i<bus.getRoadStops().size(); i++){
                if(!bus.getRoadStops().get(i).isReached()){
                    nextStop = bus.getRoadStops().get(i);
                    break;
                }
            }

            if (Math.pow(bus.getLongitude() - nextStop.getWaypoint().getLongitude(),2) +
                    Math.pow(bus.getLatitude() - nextStop.getWaypoint().getLatitude(),2) <= Constants.NEAR_RADIUS){
                int k = 0;
                for (RoadStops roadStops:
                        bus.getRoadStops()) {
                    if(nextStop.getWaypoint().equals(roadStops.getWaypoint())){
                        roadStops.setReached(true);
                        bus.setNextStop(nextStop.getWaypoint().getStop());
                        for(int i = 0; i<bus.getRoadStops().size(); i++){
                            if(!bus.getRoadStops().get(i).isReached()){
                                nextStop = bus.getRoadStops().get(i);
                                break;
                            }
                        }
                    }
                    if(roadStops.isReached())
                        k++;
                }
                if(k == bus.getRoadStops().size()){
                    bus.setStatus(BusStatus.READY);
                    //notify schedule??
                }
            }
        }

        return save(bus);
    }

    public List<BusDTO> getNearBuses(Stop stop) {
        var buses = busRepository.findAll();
        var result = new ArrayList<BusDTO>();
        for (Bus bus:
             buses) {
            if(bus.getNextStop()!=null && bus.getNextStop().equals(stop)){
                var busDTO = new BusDTO();
                busDTO.setBusId(bus.getId());
                busDTO.setLongitude(bus.getLongitude());
                busDTO.setLatitude(bus.getLatitude());
                busDTO.setRouteTitle(bus.getRoute().getTitle());
                var position = new PosDTO();
                position.setLongitude(bus.getNextStop().getLongitude());
                position.setLatitude(bus.getNextStop().getLatitude());
                busDTO.setMinutesToNextStop(
                        routeService.getAlmostRealWaypointToStopTime(
                                bus.getRoute(),
                                position,
                                stop
                        )
                );
            }
        }
        return result;
    }
}
