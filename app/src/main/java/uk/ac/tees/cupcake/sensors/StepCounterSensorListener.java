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
    
    private long referenceTime;
    
    private final Context context;
    
    private int lastEventCount;
    
    public StepCounterSensorListener(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(ApplicationConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        final int eventValue = (int) event.values[0];
        final int storedSteps = preferences.getInt("steps", 0);
        final int storedTime = preferences.getInt("steps_time", 0);
        final int delta = eventValue - referenceStepCount;
        final int stepCount = storedSteps + delta;
        
        if (!firstEvent) {
            if (delta <= ApplicationConstants.STEP_COUNTING_EVENT_START_THRESHOLD) {
                int time = (int) (System.currentTimeMillis() - referenceTime);
                
                if (lastEventCount > 0) {
                    time *= lastEventCount;
                    lastEventCount = 0;
                }
                
                preferences.edit().putInt("steps_time", storedTime + time).apply();
            } else {
                lastEventCount = delta;
            }
            
            referenceTime = System.currentTimeMillis();
            preferences.edit().putInt("steps", stepCount).apply();
        } else {
            firstEvent = false;
        }
    
        context.sendBroadcast(new Intent(ApplicationConstants.STEP_COUNT_BROADCAST_INTENT_ACTION));
        
        referenceStepCount = eventValue;
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}