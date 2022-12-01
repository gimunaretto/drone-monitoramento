import DroneControl.Drone
import java.util.Timer;
import java.util.TimerTask;

fun main(args: Array<String>) {

    var i = 0;
    var drone1 = Drone()
    var drone2 = Drone()

    Timer().scheduleAtFixedRate( object : TimerTask() {
        override fun run() {
            println(drone1.readValues())
            println(drone2.readValues())
            i++;
            drone1.tracker = (i<5);
            drone2.tracker = (i<10);
        }
    }, 0, 2000)

}
