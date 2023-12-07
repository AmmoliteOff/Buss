package org.hackathon.buss.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.hackathon.buss.model.Event;
import org.hackathon.buss.model.Message;
import org.hackathon.buss.service.ChatService;
import org.hackathon.buss.service.EventService;
import org.hackathon.buss.service.MessageService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final EventService eventService;
    private final MessageService messageService;
    private final ChatService chatService;


    @MessageMapping("/sendMessage/{id}")
    @SendTo("/topic/sendMessage/{id}")
    public Message sendMessage(@DestinationVariable Long id, Message messageRequest) {

    }

    @MessageMapping("/sendEvent/{id}")
    @SendTo("/topic/sendEvent/{id}")
    public Message sendMessage(@DestinationVariable Long id, Event event) {
        event = eventService.save(event);
        var msg = new Message();
        msg.setSender(event.getDriver());
        msg.setReceiver(event.getDispatcher());
        msg.setContent(event.getType().toString());
        msg.setChat(chatService.findById(id).get());
        return messageService.save(msg);
    }
}