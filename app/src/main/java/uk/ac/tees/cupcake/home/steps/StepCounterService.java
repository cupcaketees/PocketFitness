package uk.ac.tees.cupcake.home.steps;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import uk.ac.tees.cupcake.sensors.SensorAdapter;
import uk.ac.tees.cupcake.sensors.StepCounterSensorListener;

/**
 * A started non-bound service which registers the step counter listener to listen for step count events.
 *
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public final class StepCounterService extends Service {
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SensorAdapter sensorAdapter = new SensorAdapter(getApplicationContext());
        StepCounterSensorListener eventListener = new StepCounterSensorListener(getApplicationContext());
    
        sensorAdapter.addSensorWithListener(Sensor.TYPE_STEP_COUNTER, SensorManager.SENSOR_DELAY_NORMAL, eventListener);
        
        return Service.START_STICKY;
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}