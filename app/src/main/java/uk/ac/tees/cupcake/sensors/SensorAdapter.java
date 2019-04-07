package uk.ac.tees.cupcake.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides access to sensors through the {@link SensorManager} and {@link Sensor} classes.
 *
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public final class SensorAdapter {
    
    /**
     * Associates integer types with sensors.
     */
    private final Map<Integer, Sensor> sensors = new HashMap<>();
    
    /**
     * Maps {@link Sensor} ids to a {@link SensorEventListener} for tracking registered listeners.
     */
    private final Map<Integer, SensorEventListener> sensorEventListeners = new HashMap<>();
    
    /**
     * Manages the {@link Sensor}s used in this adapter.
     */
    private SensorManager sensorManager;
    
    /**
     * Constructs a new {@link SensorAdapter}, throws {@link RuntimeException} if sensor manager service is null.
     *
     * @param context the context used for getting the sensor service.
     */
    public SensorAdapter(Context context) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager == null) {
            throw new RuntimeException("No sensor service");
        }
    }
    
    /**
     * Adds the sensor for the given type.
     *
     * @param type the type of the sensor to add.
     * @return {@code true} fi the sensor is added.
     */
    public boolean addSensor(int type) {
        Sensor s = sensorManager.getDefaultSensor(type);

        if (s == null) {
            List<Sensor> sensorList = sensorManager.getSensorList(type);

            if (sensorList.isEmpty()) {
                return false;
            }

            s = sensorList.get(0);
        }

        if (!sensors.containsKey(type)) {
            sensors.put(type, s);
        }
        return true;
    }
    
    /**
     * Attempts to add a sensor and register the given event listener for the given sensor in
     * the sensor manager.
     *
     * @param type the type of sensor to add and associate the listener with.
     * @param delay the rate at which sensor events are received by the listener.
     * @param eventListener the event listener receiving sensor events.
     * @return {@code true} if the sensor is added and event listener registered successfully.
     */
    public boolean addSensorWithListener(int type, int delay, SensorEventListener eventListener) {
        return addSensor(type) && registerListener(delay, type, eventListener);
    }
    
    /**
     * Associates the specified {@link SensorEventListener} with the given type and registers the
     * event listener with {@link #sensorManager}.
     *
     * @param delay the rate at which sensor events are received by the listener.
     * @param type the type of the sensor to associate the listener with.
     * @param eventListener the event listener to register.
     * @return {@code true} if the given {@link SensorEventListener} is registered successfully.
     */
    public boolean registerListener(int delay, int type, SensorEventListener eventListener) {
        if (!sensors.containsKey(type)) {
            return false;
        }

        if (!sensorEventListeners.containsKey(type)) {
            sensorEventListeners.put(type, eventListener);

            return sensorManager.registerListener(eventListener, sensors.get(type), delay);
        }

        return false;
    }

    /**
     * Unregisters the {@link SensorEventListener} associated with the given type.
     *
     * @param type the sensor type.
     */
    public void unregisterListener(int type) {
        if (sensorEventListeners.containsKey(type)) {
            sensorManager.unregisterListener(sensorEventListeners.get(type));
            sensorEventListeners.remove(type);
        }
    }
    
}