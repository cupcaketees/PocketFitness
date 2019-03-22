package uk.ac.tees.cupcake.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import uk.ac.tees.cupcake.R;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */

public class StepCounterSensorListener implements SensorEventListener {
    
    /**
     * The step counter view.
     */
    private final View stepCounterView;
    
    private TextView steps;
    
    private TextView distance;
    
    /**
     * Constructs a new {@link StepCounterSensorListener}
     */
    public StepCounterSensorListener(View stepCounterView) {
        this.stepCounterView = stepCounterView;
        
        steps = stepCounterView.findViewById(R.id.home_steps_text);
        distance = stepCounterView.findViewById(R.id.home_steps_distance);
    }
    
    private static final double STEPS_PER_MILE = 5280 / 2.5d;
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] data = event.values;
        
        int stepCount = (int) data[0];
        String stepsText = Integer.toString(stepCount);
        steps.setText(stepsText);
        
        String dist = String.format("%.2f", stepCount / STEPS_PER_MILE);
        distance.setText(dist);
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Toast.makeText(stepCounterView.getContext(), "" + accuracy, Toast.LENGTH_SHORT).show();
    }
}