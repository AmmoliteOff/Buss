package org.hackathon.buss.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;

import org.hackathon.buss.model.Event;
import org.hackathon.buss.model.User;
import org.hackathon.buss.service.UserService;
import org.hackathon.buss.util.view.DetailedInformation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users",
                produces = MediaType.APPLICATION_JSON_VALUE,
                consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @JsonView({DetailedInformation.class})
    public ResponseEntity<User> getUser(@PathVariable("id") Long userId) {
        User user = userService.findById(userId).orElse(null);
        return ResponseEntity.ok(user);
    }

}
