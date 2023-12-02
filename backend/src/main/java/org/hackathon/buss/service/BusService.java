package org.hackathon.buss.service;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.dto.BusDTO;
import org.hackathon.buss.enums.BusStatus;
import org.hackathon.buss.model.Bus;
import org.hackathon.buss.repository.BusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BusService {

    private final BusRepository busRepository;
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

    public Bus update(BusDTO busDTO) {
        var bus = busRepository.findById(busDTO.getBusId()).get();
        bus.setCharge(busDTO.getCharge());
        bus.setLatitude(busDTO.getLatitude());
        bus.setLongitude(busDTO.getLongitude());
        if(bus.getStatus() == BusStatus.CHARGING && bus.getCharge() >=80){
            bus.setStatus(BusStatus.READY);
        }
        return save(bus);
//        if(bus.getStatus() == BusStatus.CHARGING && bus.getCharge() >=80){
//            bus.setStatus(BusStatus.READY);
//            bus = save(bus);
//            scheduleService.returnBus(bus);
//            return bus;
//        }
//        else
//            return save(bus);
    }
}
