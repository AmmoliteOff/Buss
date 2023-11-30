package org.hackathon.buss.repository;

import org.hackathon.buss.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByUsername(String username);


    @Query("SELECT em.receiver FROM EventMessage em " +
            "GROUP BY em.receiver " +
            "ORDER BY COUNT(em.receiver) ASC")
    List<User> findDispatcher();
}
