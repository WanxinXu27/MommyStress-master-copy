package thehabitslab.com.codebase;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.os.SystemClock;
import java.util.Calendar;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity {
    private static final String TAG = "Main Activity";
    private static int API_Version;

    // Sensor related fields
    private Sensor mAccel;
    private SensorManager mManager;
    private TextView statusText;
    /**
     * Maintains accelerometer registration state.
     * Update every time you register/unregister outside of
     * activity lifecycle
     */
    private boolean accelIsRegistered = false;
    private DataAccumulator dataManager = new DataAccumulator();
    private CSVManager csvManager = new CSVManager();

    public static WakeLock wakeLock;

    /* ***************************** ACTIVITY CONTROL METHODS ********************************** */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        API_Version = android.os.Build.VERSION.SDK_INT;

        setContentView(R.layout.activity_main);

        mManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccel = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        statusText = (TextView) findViewById(R.id.status);

        checkPermissions();

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");
        wakeLock.acquire();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        wakeLock.release();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Check if the accelerometer was streaming before paused
//        if (accelIsRegistered) {
//            mManager.registerListener(accelListener, mAccel, SensorManager.SENSOR_DELAY_NORMAL);
//            statusText.setText(R.string.text_active);
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        // Check if accelerometer should be paused
//        if (accelIsRegistered) {
//            mManager.unregisterListener(accelListener);
//            statusText.setText(R.string.text_inactive);
//        }
//    }


    /* ****************************** USER INTERACTION HANDLING ******************************** */

    /**
     * Called when the accelerometer button is clicked
     */
    public void toggleAccelClicked(View v) {
        // Toggle streaming on or off depending of previous state
        if (accelIsRegistered) {
            mManager.unregisterListener(accelListener);
            statusText.setText(R.string.text_inactive);
        } else {
            mManager.registerListener(accelListener, mAccel, SensorManager.SENSOR_DELAY_NORMAL);
            statusText.setText(R.string.text_active);
        }

        accelIsRegistered = !accelIsRegistered;
    }

    /**
     * Called when show document button is clicked
     */
    public void showDocumentClicked(View v) {
        String content = csvManager.getDataAsString();
        ((TextView) findViewById(R.id.docText)).setText(content);
    }


    /* ********************************** WORKING PARTS **************************************** */
    /**
     * Custom implementation of SensorEventListener specific to what we want to do with
     * accelerometer data.
     */
    private SensorEventListener accelListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // Handle new accel value
            List<SensorData> data;
            Log.w("sensor", "collecting" + event.timestamp);

            if (API_Version == 22) {
                event.timestamp = event.timestamp/1000000L;
            }
            else {
                Calendar c = Calendar.getInstance();
                event.timestamp = c.getTimeInMillis() + (event.timestamp - SystemClock.elapsedRealtimeNanos()) / 1000000L;
            }

            Log.w("sensor", "collecting" + event.timestamp);

            if ((data = dataManager.addEvent(event)) != null)
                handleData(data);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Handle change of accuracy
            Log.w(TAG, "Accuracy of accelerometer changed... This was unexpected.");
        }

        /**
         * Performs operations on the energy value once it is obtained
         * @param data List of sensor events to be saved
         */
        private void handleData(List<SensorData> data) {
            Log.w(TAG, "Saving data for " + data.size() + " entries");
            csvManager.saveData(data);
        }
    };


    // Request permisions necessary
    private static final int PERMISSIONS_ALL = 1;

    private void checkPermissions() {
        // Check android permissions
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, PERMISSIONS_ALL);
            }
        }
    }
}
