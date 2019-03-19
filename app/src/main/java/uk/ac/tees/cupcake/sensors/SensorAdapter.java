package uk.ac.tees.cupcake.sensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A generalised activity that uses some sensor.
 *
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public final class SensorAdapter {
    
    private final Activity activity;
    
    private final int[] sensorTypes;
    
    private final Map<Integer, Sensor> sensors = new HashMap<>();
    
    private SensorManager sensorManager;
    
    public SensorAdapter(Activity activity, int...sensorTypes) {
        this.activity = activity;
        this.sensorTypes = sensorTypes;
    }
    
    public final void onCreate() {
        if (!setupSensors()) {
            activity.finish();
        }
    }
    
    private boolean setupSensors() {
        if ((sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE)) == null) {
            throw new RuntimeException("No sensor service");
        }
        
        for (int sensorType : sensorTypes) {
            if (!addSensor(sensorType)) {
                return false;
            }
        }

        return true;
    }
    
    public boolean addSensor(int type) {
        Sensor s = sensorManager.getDefaultSensor(type);
    
        if (s == null) {
            List<Sensor> sensorList = sensorManager.getSensorList(type);
        
            if (sensorList.isEmpty()) {
                return false;
            }
        
            s = sensorList.get(0);
        }
    
        sensors.put(type, s);
        return true;
    }
    
    public boolean registerListener(int delay, int sensor, SensorEventListener eventListener) {
        if (!sensors.containsKey(sensor)) {
            return false;
        }
    
        return sensorManager.registerListener(eventListener, sensors.get(sensor), delay);
    }
    
    public void unregisterListener(int sensor, SensorEventListener eventListener) {
        if (sensors.containsKey(sensor)) {
            sensorManager.unregisterListener(eventListener);
        }
    }
    
}