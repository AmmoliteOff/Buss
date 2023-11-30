package org.hackathon.buss.controller;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.model.Message;
import org.hackathon.buss.model.User;
import org.hackathon.buss.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage (@RequestBody Message message,
                                                @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(messageService.save(message));
    }
}
