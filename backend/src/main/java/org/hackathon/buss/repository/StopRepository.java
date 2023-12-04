package org.hackathon.buss.repository;

import org.hackathon.buss.model.Stop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StopRepository extends JpaRepository<Stop, Long>{

    Optional<Stop> findByTitle(String title);
}
