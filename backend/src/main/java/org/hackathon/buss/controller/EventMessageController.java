package org.hackathon.buss.controller;

import lombok.RequiredArgsConstructor;
import org.hackathon.buss.service.UserService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event-messages")
public class EventMessageController {

    private final UserService userService;

//    private final EventMessageService eventMessageService;

//    @PostMapping("/report")
//    public ResponseEntity<?> reportAnEvent (@RequestBody Event event,
//                                            @AuthenticationPrincipal User user) {
//        if(user.getRole().equals(Role.DRIVER)) {
//            User dispatcher = userService.findDispatcher().stream().findFirst().orElseThrow();
//            eventMessage.setReceiver(dispatcher);
//            return ResponseEntity.ok(eventMessageService.save(eventMessage));
//        } else {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have enough rights");
//        }
//    }
//
//    @PatchMapping("/process")
//    public ResponseEntity<?> processAnEvent (@RequestBody EventMessage eventMessage,
//                                             @AuthenticationPrincipal User user) {
//        EventMessage processedEvent = eventMessageService.findById(eventMessage.getId()).orElseThrow();
//        processedEvent.setProcessed(true);
//        return ResponseEntity.ok(eventMessageService.save(processedEvent));
//    }
}

