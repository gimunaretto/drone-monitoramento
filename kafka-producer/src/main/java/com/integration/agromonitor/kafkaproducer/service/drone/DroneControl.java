package com.integration.agromonitor.kafkaproducer.service.drone;

import com.integration.agromonitor.kafkaproducer.service.email.EmailController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class DroneControl {

    @Autowired
    EmailController emailController;
    private static List<Drone> drones = new  ArrayList<Drone>();

    private Boolean isAlertTemp = true;
    private Boolean isAlertUmid = true;

    public void addDrone(){
        drones.add( new Drone("DRONE #" + String.format("%03d", drones.size()+1)) );
    }

    public int countDrones() {
        return drones.size();
    }


    public Drone getDrone(int indexDrone) {
        if (indexDrone < countDrones()) {
            return drones.get(indexDrone);
        }
        return null;
    }

    public void setTracker(int indexDrone) {
        if (indexDrone < countDrones()) {
            drones.get(indexDrone).setTracker();
        }
    }

    public String getMessages() {
        StringBuilder response = new StringBuilder();
        StringBuilder alertas = new StringBuilder();

        for ( Drone drone: drones) {
            drone.sensor.calculateSensorValues();
            response.append( String.format("%n# %s >> %s - Tracker: %s",
                    LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                    drone.getName(), drone.getTracker()));
            if (drone.getTracker().equals("ON")) {
                drone.calculateRandomLocation();
                response.append(String.format(" * Latitude: [%f] - Longitude: [%f] => [ Temperatura = %sº, Umidade = %s ]",
                        drone.getLatitude(),
                        drone.getLongitude(),
                        drone.sensor.getTemperatura(),
                        drone.sensor.getUmidade()
                ));
                while (!drone.sensor.listaAlertas.isEmpty()) {
                    response.append("\n");
                    response.append(drone.name + " " + drone.sensor.listaAlertas.element());  // não remove da lista...
                    alertas.append(drone.name + " "  + drone.sensor.listaAlertas.poll());  // remove....
                    alertas.append("\n");
                }
            } else {
                response.append("\n");
            }

        }

        sendAlerta(alertas.toString());

        return response.toString();

    }

    private void sendAlerta(String mensagem) {
        if (!mensagem.isEmpty()){
            emailController.sendEmail(mensagem);
        }
    }


}
