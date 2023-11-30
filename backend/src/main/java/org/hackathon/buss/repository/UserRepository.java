package org.hackathon.buss.repository;

import org.hackathon.buss.enums.Role;
import org.hackathon.buss.model.Dispatcher;
import org.hackathon.buss.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<Dispatcher> findAllByRole(Role role);
}
