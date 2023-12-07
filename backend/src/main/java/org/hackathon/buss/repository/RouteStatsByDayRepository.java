package org.hackathon.buss.repository;

import org.hackathon.buss.model.Route;
import org.hackathon.buss.model.stats.RouteStatsByDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RouteStatsByDayRepository extends JpaRepository<RouteStatsByDay, Long> {

     Optional<RouteStatsByDay> findRouteStatsByRouteAndRouteStatsByDayId(Route route, Long id);
}
