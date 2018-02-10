package thehabitslab.com.codebase;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import java.util.Arrays;

/**
 * Created by andrey on 1/23/18.
 */

public class SensorData {
    public long timestamp;
    public float[] values;


    SensorData(SensorData e) {
        timestamp = e.timestamp;
        values = Arrays.copyOf(e.values, e.values.length);
    }

    public SensorData(SensorEvent e) {
        timestamp = e.timestamp;
        values = Arrays.copyOf(e.values, e.values.length);
    }

}
