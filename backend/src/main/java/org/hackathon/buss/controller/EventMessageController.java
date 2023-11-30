package org.hackathon.buss.controller;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.enums.Role;
import org.hackathon.buss.model.EventMessage;
import org.hackathon.buss.model.User;
import org.hackathon.buss.service.EventMessageService;
import org.hackathon.buss.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event-messages")
public class EventMessageController {

    private final UserService userService;

    private final EventMessageService eventMessageService;

    @PostMapping("/report")
    public ResponseEntity<?> reportAnEvent (@RequestBody EventMessage eventMessage,
                                            @AuthenticationPrincipal User user) {
        if(user.getRole().equals(Role.DRIVER)) {
            User dispatcher = userService.findDispatcher().stream().findFirst().orElseThrow();
            eventMessage.setReceiver(dispatcher);
            return ResponseEntity.ok(eventMessageService.save(eventMessage));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have enough rights");
        }
    }

    @PostMapping("/process")
    public ResponseEntity<?> processAnEvent (@RequestBody EventMessage eventMessage,
                                            @AuthenticationPrincipal User user) {
//        if(user.getRole().equals(Role.DRIVER)) {
//            User dispatcher = userService.findDispatcher().orElseThrow();
//            eventMessage.setReceiver(dispatcher);
//            return ResponseEntity.ok(eventMessageService.save(eventMessage));
//        } else {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have enough rights");
//        }
        return ResponseEntity.ok("");
    }}
