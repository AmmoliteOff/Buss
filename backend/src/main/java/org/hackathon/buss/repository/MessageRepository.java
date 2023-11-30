package org.hackathon.buss.repository;

import org.hackathon.buss.model.EventMessage;
import org.hackathon.buss.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
