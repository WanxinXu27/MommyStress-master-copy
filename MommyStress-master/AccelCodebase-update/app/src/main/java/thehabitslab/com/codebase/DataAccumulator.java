package thehabitslab.com.codebase;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import java.util.LinkedList;
import java.util.List;
import android.util.Log;

/**
 * Accumulates data and keeps it for the specified time interval.
 * Created by William on 12/7/2016.
 */
public class DataAccumulator {
    private static final String TAG = "DataAccumulator";
    private LinkedList<SensorData> events = new LinkedList<>();
    /**
     * Interval for which the energy is calculated (in milliseconds)
     * 60000 corresponds to a minute
     */
    private final long TIME_INTERVAL = 5000; // 5000 corresponds to 5 sec

    /**
     * Adds the given event to the list
     * If TIME_INTERVAL  has passed since first element, return the list of data.
     *
     * @param sensorEvent The accelerometer event to be added
     * @return null if a minute has not passed,
     * energy reading with the sum squared variance if a minute has passed
     */
    public List<SensorData> addEvent(SensorEvent sensorEvent) {
        events.addLast(new SensorData(sensorEvent));

        if ((sensorEvent.timestamp - events.getFirst().timestamp) < TIME_INTERVAL) {
            // Interval has not passed
            return null;
        } else {
            Log.w("DataAccumulator", "returning the buffer");
            // A minute has passed, replace and return the list
            LinkedList<SensorData> tmp = events;
            events = new LinkedList<SensorData>();

            return tmp;
        }
    }
}
