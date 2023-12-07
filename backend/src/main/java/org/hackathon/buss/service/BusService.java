package org.hackathon.buss.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hackathon.buss.dto.BusDTO;
import org.hackathon.buss.dto.PosDTO;
import org.hackathon.buss.enums.BusStatus;
import org.hackathon.buss.model.Bus;
import org.hackathon.buss.model.Route;
import org.hackathon.buss.model.Stop;
import org.hackathon.buss.repository.BusRepository;
import org.hackathon.buss.repository.RoadStopsRepository;
import org.hackathon.buss.model.RoadStops;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hackathon.buss.util.Constants.NEAR_RADIUS;

@Service
@AllArgsConstructor
public class BusService {

    private final BusRepository busRepository;
    private final RouteService routeService;
    private final RoadStopsRepository roadStopsRepository;
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
        bus.setCharge(busDTO.getCharge());
        if(busDTO.isCharging())
            bus.setStatus(BusStatus.CHARGING);

        if(bus.getStatus() == BusStatus.IN_ROAD) {
            if(Math.pow(bus.getNextStop().getLatitude()-busDTO.getLatitude(),2) +
                    Math.pow(bus.getNextStop().getLongitude()-busDTO.getLongitude(),2) <= NEAR_RADIUS){
                int k = 0;
                boolean flag = true;

                while(flag){
                    if(!bus.getRoadStops().get(k).isReached()){
                        bus.getRoadStops().get(k).setReached(true);
                        flag = false;
                    }
                    k++;
                }

                k = 0;
                flag = true;
                RoadStops rs = null;
                for (RoadStops rsl:
                        roadStopsRepository.findAllByBus(bus)){
                    if(rsl.getStop().equals(bus.getNextStop()))
                        rs = rsl;
                }

                while(flag){
                    if(!bus.getRoadStops().get(k).isReached()){
                        if(bus.getRoadStops().get(k).getOrderInList() == rs.getOrderInList()+1) {
                            bus.setNextStop(bus.getRoadStops().get(k).getStop());
                            flag = false;
                        }
                    }
                    k++;
                }
                if(k == bus.getRoadStops().size())
                    bus.setRoadStops(new ArrayList<>());
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
                busDTO.setCharge(bus.getCharge());
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
                result.add(busDTO);
            }
        }
        return result;
    }
}
