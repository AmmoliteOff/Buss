package org.hackathon.buss.controller;

import lombok.RequiredArgsConstructor;

import org.hackathon.buss.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


}
