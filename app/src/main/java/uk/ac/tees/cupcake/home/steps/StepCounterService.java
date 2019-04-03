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
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */

public final class StepCounterService extends Service {
    
    private SensorAdapter sensorAdapter;
    
    private StepCounterSensorListener eventListener;
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorAdapter = new SensorAdapter(getApplicationContext(), Sensor.TYPE_STEP_COUNTER);
        eventListener = new StepCounterSensorListener(getApplicationContext());
    
        if (sensorAdapter.setupSensors()) {
            sensorAdapter.registerListener(SensorManager.SENSOR_DELAY_NORMAL, Sensor.TYPE_STEP_COUNTER, eventListener);
        }
        
        return Service.START_STICKY;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        
        sensorAdapter.unregisterListener(Sensor.TYPE_STEP_COUNTER, eventListener);
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}