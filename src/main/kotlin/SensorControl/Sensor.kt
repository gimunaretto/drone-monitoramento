package SensorControl

class Sensor {
    fun generate(): Pair<Int, Int> =
        (-25..0).random() to (0..10).random()  // ##MOCK
        //(-25..0).random() to (0..100).random()
}