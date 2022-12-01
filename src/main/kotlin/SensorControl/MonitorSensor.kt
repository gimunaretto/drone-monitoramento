package SensorControl

class MonitorSensor {

    private val sensor = Sensor()
    private var startTime = System.currentTimeMillis()
    private var isAlertTemp = true
    private var isAlertUmid = true

    fun readValues(): String {
        val (temperatura, umidade) = sensor.generate()
        return (" * Temperatura = $temperatura, Umidade = $umidade").plus(alert(temperatura, umidade))
    }

    private fun alert(t: Int, u: Int): String {
        var resultTemp = ""
        var resultUmid = ""
        if ( ((System.currentTimeMillis() - startTime) / 1000) <= 10 ) {
            when(t) { !in 1..34 ->  isAlertTemp = !isAlertTemp ?: true }
            when(u) { in 0..15  ->  isAlertUmid = !isAlertUmid ?: true }
        } else {
            if (isAlertTemp) { resultTemp = "\t ## Temperatura(>= 35 ou <=0): $t" }
            if (isAlertUmid) { resultUmid = "\t ** Umidade(<= 15%): $u" }
            startTime = System.currentTimeMillis()
            isAlertTemp = false
            isAlertUmid = false
        }
        return resultTemp.plus(resultUmid)
    }

}