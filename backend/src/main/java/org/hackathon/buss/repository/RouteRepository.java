package org.hackathon.buss.repository;

import org.hackathon.buss.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
}
