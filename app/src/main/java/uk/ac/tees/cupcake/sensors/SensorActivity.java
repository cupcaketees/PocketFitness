package uk.ac.tees.cupcake.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import uk.ac.tees.cupcake.R;

/**
 * A generalised activity that uses some sensor.
 *
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public abstract class SensorActivity extends AppCompatActivity {
    
    /**
     * {@link Sensor} that we listen for events on.
     */
    protected Sensor sensor;
    
    /**
     * {@link SensorManager} gives access to a particular sensor.
     */
    protected SensorManager sensorManager;
    
    /**
     * Whether the heart rate sensor exists.
     */
    private boolean hasSensor;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (setupSensor()) {
            setup();
            
        } else {
            finish();
            
            Toast.makeText(this, getString(R.string.sensor_no_sensor, sensorType()), Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        if (hasSensor) {
            sensorManager.registerListener(eventListener(), sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
        if (hasSensor) {
            sensorManager.unregisterListener(eventListener());
        }
    }
    
    /**
     * Gets a heart rate sensor if one is available and assigns {@link #sensor} to it.
     *
     * @return {@code true} if sensor is successfully acquired.
     */
    private boolean setupSensor() {
        if ((sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE)) == null) {
            throw new RuntimeException("No sensor service");
        }
        
        sensor = sensorManager.getDefaultSensor(sensorType());
        
        return hasSensor = (sensor == null);
    }
    
    /**
     * This method is called in the {@link #onCreate}
     */
    public abstract void setup();
    
    /**
     * Poor design but necessary due to no-args constructor only restriction.
     *
     * <p>Use {@code Sensor.TYPE_*}</p>
     *
     * @return the type of sensor to use for this activity.
     */
    public abstract int sensorType();
    
    /**
     * Poor design but necessary due to no-args constructor only restriction.
     *
     * <p>Handles {@link SensorEvent}s from the {@link #sensor}.</p>
     *
     * @return the event listener that's called when event is received.
     */
    public abstract SensorEventListener eventListener();
    
}