package com.integration.agromonitor.kafkaproducer.service.drone;

import com.integration.agromonitor.kafkaproducer.service.sensor.Sensor;

import java.util.Random;

public class Drone {

    public Sensor sensor;
    final Double LATITUDE_CENTRO_BR = -10.18311;
    final Double LONGITUDE_CENTRO_BR = -48.33366;
    final Double RAIO_OPERACAO = 1000.0 * 1200.0;
    private Boolean tracker = true;

    public String name;
    public Double latitude;
    public Double longitude;

    public Double getLatitude() {
        return latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public String getName() {
        return name;
    }

    public String getTracker() {
        return tracker? "ON" : "OFF";
    }

    public Drone(String nameDrone) {
        sensor = new Sensor();
        name = nameDrone;
    }

    public void setTracker() {
        tracker = !tracker;
    }

    public void calculateRandomLocation() {
        Double y0 = LATITUDE_CENTRO_BR;
        Double x0 = LONGITUDE_CENTRO_BR;

        // de metros para graus
        Double radiusInDegrees = RAIO_OPERACAO / 111320f;

        // distancia randomica a partir do eixo.
        Random random = new Random();
        Double u = random.nextDouble();
        Double v = random.nextDouble();
        Double w = radiusInDegrees * Math.sqrt(u);
        Double t = 2 * Math.PI * v;

        // deltas de x e y..
        Double x = w * Math.cos(t);
        Double y = w * Math.sin(t);

        // Compensação de X!
        Double new_x = x / Math.cos(Math.toRadians(y0));

        latitude = (y0 + y);
        longitude = (x0 + new_x);
    }

}

