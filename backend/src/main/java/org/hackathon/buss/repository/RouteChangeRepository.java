package org.hackathon.buss.repository;


import org.hackathon.buss.model.RouteChange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteChangeRepository extends JpaRepository<RouteChange, Long> {
}
