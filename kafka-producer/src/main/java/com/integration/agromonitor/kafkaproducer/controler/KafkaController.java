package com.integration.agromonitor.kafkaproducer.controler;

import com.integration.agromonitor.kafkaproducer.service.drone.DroneControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value="/api")
public class KafkaController {

    @Autowired
    DroneControl droneControl;

    @GetMapping("/create_drone")
    public String create(){
        droneControl.addDrone();
        return String.format(droneControl.getMessages());
    }

    @GetMapping(value = "/tracker/{idDrone}")
    public String setTracker(@PathVariable int idDrone){
        if (droneControl.countDrones() >= idDrone ) {
            droneControl.setTracker(idDrone-1);
            return String.format(droneControl.getMessages());
        }
        return String.format("Drone {%s} não localizado!",idDrone);
    }

}
