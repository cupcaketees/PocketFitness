package uk.ac.tees.cupcake.sensors;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.View;
import android.widget.TextView;

import uk.ac.tees.cupcake.R;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */

public class StepCounterSensorListener implements SensorEventListener {
    
    private TextView steps;
    
    private TextView distance;
    
    private SharedPreferences preferences;
    
    private static final double STEPS_PER_MILE = 5280 / 2.5d;
    
    private boolean firstEvent = true;
    
    /**
     * Constructs a new {@link StepCounterSensorListener}
     */
    public StepCounterSensorListener(View stepCounterView) {
        steps = stepCounterView.findViewById(R.id.home_steps_text);
        distance = stepCounterView.findViewById(R.id.home_steps_distance);
    
        preferences = stepCounterView.getContext().getSharedPreferences("CupcakePrefs", Context.MODE_PRIVATE);
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        int currentSteps = preferences.getInt("steps", 0);
        
        if (!firstEvent) {
            preferences.edit().putInt("steps", ++currentSteps).apply();
        }
    
        firstEvent = false;
        
        String stepsText = Integer.toString(currentSteps);
        steps.setText(stepsText);
    
        String dist = String.format("%.2f", currentSteps / STEPS_PER_MILE);
        distance.setText(dist);
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}