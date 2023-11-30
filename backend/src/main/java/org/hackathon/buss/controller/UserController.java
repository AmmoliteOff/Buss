package org.hackathon.buss.controller;

import lombok.RequiredArgsConstructor;

import org.hackathon.buss.model.User;
import org.hackathon.buss.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getDispatcher(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(userService.findById(userId).orElseThrow());
    }

}
