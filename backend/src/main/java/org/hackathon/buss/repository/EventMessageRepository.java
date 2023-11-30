package org.hackathon.buss.repository;

import org.hackathon.buss.model.EventMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventMessageRepository extends JpaRepository<EventMessage, Long> {
}
