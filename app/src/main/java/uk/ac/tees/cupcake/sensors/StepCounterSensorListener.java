package uk.ac.tees.cupcake.sensors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import uk.ac.tees.cupcake.ApplicationConstants;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public class StepCounterSensorListener implements SensorEventListener {
    
    private SharedPreferences preferences;
    
    private boolean firstEvent = true;
    
    private int referenceStepCount;
    
    private final Context context;
    
    public StepCounterSensorListener(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(ApplicationConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        final int eventValue = (int) event.values[0];
        final int storedSteps = preferences.getInt("steps", 0);
        int stepCount = storedSteps + (eventValue - referenceStepCount);
        
        if (!firstEvent) {
            preferences.edit().putInt("steps", stepCount).apply();
        } else {
            firstEvent = false;
        }
    
        Intent intent = new Intent(ApplicationConstants.BROADCAST_INTENT_ACTION);
        context.sendBroadcast(intent);
        
        referenceStepCount = eventValue;
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}