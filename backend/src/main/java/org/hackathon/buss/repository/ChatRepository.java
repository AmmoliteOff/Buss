package org.hackathon.buss.repository;

import org.hackathon.buss.model.Chat;
import org.hackathon.buss.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findByDriver(Driver driver);
}
