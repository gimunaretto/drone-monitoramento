package com.integration.agromonitor.kafkaproducer.service.sensor;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Queue;

@Component
public class Sensor {
    private final int TEMPERATURA_MINIMA = -25;
    private final int TEMPERATURA_MAXIMA = -1;//40;
    private final int UMIDADE_MINIMA = 0;
    private final int UMIDADE_MAXIMA = 14;//100;
    private final int ALERTA_TEMPERATURA_INFERIOR = 0;
    private final int ALERTA_TEMPERATURA_SUPERIOR = 35;
    private final int ALERTA_UMIDADE = 15;
    private final int TEMPO_MONITORAMENTO = 1000 * 60 * 1; // 1 minuto

    public Queue<String> listaAlertas;
    int temperatura;
    int umidade;
    Long startTimeMonitoramento = System.currentTimeMillis();

    Boolean alertaTemperatura = false;

    Boolean alertaUmidade = false;

    public Sensor() {
        listaAlertas = new LinkedList<String>();
        calculateSensorValues();
    }

    public void calculateSensorValues() {
        temperatura = (int) ((Math.random() * (TEMPERATURA_MAXIMA - TEMPERATURA_MINIMA)) + TEMPERATURA_MINIMA);
        umidade = (int) ((Math.random() * (UMIDADE_MAXIMA - UMIDADE_MINIMA)) + UMIDADE_MINIMA);
        alert();
    }

    private void alert() {
        if ( ((System.currentTimeMillis() - startTimeMonitoramento)) <= TEMPO_MONITORAMENTO ) {
            if (!alertaTemperatura)
                setAlertaTemperatura(((temperatura <= ALERTA_TEMPERATURA_INFERIOR || temperatura >= ALERTA_TEMPERATURA_SUPERIOR)));
            if (!alertaUmidade)
                setAlertaUmidade((umidade <= ALERTA_UMIDADE));
        } else {

            if (alertaTemperatura)
                listaAlertas.add("#ALERTA TEMPERATURA:(>= 35º ou <=0º) * " +
                    LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

            if (alertaUmidade)
                listaAlertas.add("#ALERTA UMIDADE:(<=15%) * " +
                        LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

            alertaTemperatura = false;
            alertaUmidade = false;
            startTimeMonitoramento = System.currentTimeMillis();
        }
    }


    public int getTemperatura() {
        return temperatura;
    }
    public int getUmidade() {
        return umidade;
    }

    public Boolean getAlertaTemperatura() {
        return alertaTemperatura;
    }
    public Boolean getAlertaUmidade() {
        return alertaUmidade;
    }
    public void setAlertaTemperatura(Boolean alertaTemperatura) {
        this.alertaTemperatura = alertaTemperatura;
    }

    public void setAlertaUmidade(Boolean alertaUmidade) {
        this.alertaUmidade = alertaUmidade;
    }


}