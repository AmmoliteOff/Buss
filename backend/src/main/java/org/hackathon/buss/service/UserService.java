package org.hackathon.buss.service;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.enums.Role;
import org.hackathon.buss.model.Chat;
import org.hackathon.buss.model.Dispatcher;
import org.hackathon.buss.model.Driver;
import org.hackathon.buss.model.User;
import org.hackathon.buss.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User save(User user) {
        if(user.getRole().equals(Role.DRIVER)) {
            Chat chat = new Chat();
            chat.setDriver((Driver) user);
        }
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Dispatcher findMinLoadedDispatcher() {
        List<Dispatcher> dispatchers = userRepository.findAllByRole(Role.DISPATCHER);
        Dispatcher minLoadeddDispatcher = null;
        int minLoad = Integer.MAX_VALUE;
        for (Dispatcher dispatcher : dispatchers) {
            int load = dispatcher.getEvents().size();
            if(load < minLoad) {
                minLoad = load;
                minLoadeddDispatcher = dispatcher;
            }
        }
        return minLoadeddDispatcher == null ? dispatchers.get(0) : minLoadeddDispatcher;
    }
}
