package com.integration.agromonitor.kafkaproducer.service.messageria;

import com.integration.agromonitor.kafkaproducer.producer.MessageProducer;
import com.integration.agromonitor.kafkaproducer.service.drone.DroneControl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class SendMessages {

    @Autowired
    DroneControl droneControl;
    private final MessageProducer messageProducer;

    @Scheduled(fixedRate = 5000)
    public void send(){
        String message = null;
        message = droneControl.getMessages();
        if (!message.isEmpty())
            messageProducer.send(message);
    }



}
