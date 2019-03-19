package uk.ac.tees.cupcake.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.navigation.NavigationBarActivity;

/**
 * A generalised activity that uses some sensor.
 *
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public abstract class SensorActivity extends NavigationBarActivity {
    
    /**
     * {@link Sensor} that we listen for events on.
     */
    protected Sensor sensor;
    
    /**
     * {@link SensorManager} gives access to a particular sensor.
     */
    protected SensorManager sensorManager;
    
    /**
     * The {@link SensorEventListener} that listens for {@link SensorEvent}s from the sensor.
     */
    private SensorEventListener eventListener;
    
    /**
     * Whether the heart rate sensor exists.
     */
    private boolean hasSensor;
    
    @Override
    public final void setup() {
        if (!setupSensor()) {
            finish();
            
            Toast.makeText(this, getString(R.string.sensor_no_sensor, sensorType()), Toast.LENGTH_SHORT).show();
        } else {
            
            setupLayout();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        if (hasSensor) {
            sensorManager.registerListener(eventListener, sensor, delay());
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
        if (hasSensor) {
            sensorManager.unregisterListener(eventListener);
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
        eventListener = eventListener();
        
        return hasSensor = (sensor != null);
    }
    
    /**
     * Called in setup. Only layout related code should be invoked within this method.
     */
    public abstract void setupLayout();
    
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
     * This method is only invoked once where {@link #eventListener} is assigned
     * the return value of this method.
     *
     * <p>Creates a {@link SensorEventListener} that handles {@link SensorEvent}s from the {@link #sensor}.</p>
     *
     * @return the event listener that's called when event is received.
     */
    public abstract SensorEventListener eventListener();
    
    /**
     * The guide delay at which {@link SensorEvent}s will be received from the sensor.
     * Use one of SensorManager.SENSOR_DELAY_*.
     *
     * @return sensor event delay.
     */
    public abstract int delay();
    
}