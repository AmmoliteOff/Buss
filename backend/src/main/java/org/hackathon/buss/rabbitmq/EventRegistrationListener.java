package org.hackathon.buss.rabbitmq;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hackathon.buss.model.Event;
import org.hackathon.buss.service.EventService;
import org.hackathon.buss.util.view.NonDetailedInformation;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventRegistrationListener {

    private final EventService eventService;

    @RabbitListener(queues = {"event-registration"}, concurrency = "5")
    @JsonView(NonDetailedInformation.class)
    public void onEventRegistration(Event event) throws InterruptedException {
        Thread.sleep(3000);
        log.info("Event Registration Received");
    }
}