package org.hackathon.buss.repository;

import org.hackathon.buss.model.Waypoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WaypointRepository extends JpaRepository<Waypoint, Long> {

    Optional<Waypoint> findByLatitudeAndLongitude(double latitude, double longitude);
}
