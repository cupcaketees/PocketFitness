package uk.ac.tees.cupcake.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.View;
import android.widget.TextView;

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
    
    /**
     * Constructs a new {@link StepCounterSensorListener}
     */
    public StepCounterSensorListener(View stepCounterView) {
        this.stepCounterView = stepCounterView;
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] data = event.values;
        
        TextView heartRateAverage = stepCounterView.findViewById(R.id.step_counter_steps);
        heartRateAverage.setText(stepCounterView.getContext().getString(R.string.step_counter_steps_text, Arrays.toString(data)));
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    
    }
}