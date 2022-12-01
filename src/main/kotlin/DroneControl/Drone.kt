package DroneControl

import SensorControl.MonitorSensor
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

/*
  TODO: CRIAR NOME SEQUENCIAL DO DRONE;
 */

class Drone {
    val monitor = MonitorSensor()
    var tracker: Boolean = true

    fun getStatus(): String = if (tracker) "ON" else "OFF"

    fun readValues(): String {
        var s = "■ DRONE #{${this.hashCode()}} >> Tracker: ${getStatus()}"
        if (tracker) {
            var LATITUDE_CENTRO_BR = -10.18311
            var LONGITUDE_CENTRO_BR = -48.33366
            var RAIO_OPERACAO = 1000.0 * 1200.0
            val (latitude, longitude) = getRandomLocation(LATITUDE_CENTRO_BR,  LONGITUDE_CENTRO_BR, RAIO_OPERACAO)
            s += (" * Latitude: {$latitude} - Longitude: {$longitude} => {${monitor.readValues()}}")
        }
        return s
    }

    fun getRandomLocation(latitude: Double, longitude: Double,  raio: Double): Pair<Double, Double> {
        val y0: Double = latitude
        val x0: Double = longitude

        // de metros para graus
        val radiusInDegrees: Double = raio / 111320f

        // distancia randomica a partir do eixo.
        val u = Random.nextDouble()
        val v = Random.nextDouble()
        val w = radiusInDegrees * sqrt(u)
        val t = 2 * Math.PI * v

        // deltas de x e y..
        val x = w * cos(t)
        val y = w * sin(t)

        // Compensação de X!
        val new_x = x / cos(Math.toRadians(y0))

        return (y0 + y) to (x0 + new_x)
    }

}
