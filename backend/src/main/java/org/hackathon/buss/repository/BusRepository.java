package org.hackathon.buss.repository;

import org.hackathon.buss.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusRepository extends JpaRepository<Long, Bus>{
}
