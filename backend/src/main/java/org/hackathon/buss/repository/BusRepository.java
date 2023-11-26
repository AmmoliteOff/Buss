package org.hackathon.buss.repository;

import org.hackathon.buss.model.Bus;
import org.hackathon.buss.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long>{
    public List<Bus> findAllByRoute(Route route);
}
