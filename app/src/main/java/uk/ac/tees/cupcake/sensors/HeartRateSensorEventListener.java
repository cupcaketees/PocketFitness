package uk.ac.tees.cupcake.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Queue;

import uk.ac.tees.cupcake.R;

/**
 * Listens for {@link SensorEvent}s from the heart rate sensor and then handles the events
 * appropriately.
 *
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public class HeartRateSensorEventListener implements SensorEventListener {
    
    /**
     * The heart rate monitor view.
     */
    private final View heartRateView;
    
    /**
     * Heart rate measurements taken in one reading.
     */
    private final Queue<Float> measurements = new LinkedList<>();
    
    /**
     * Constructs a new {@link HeartRateSensorEventListener}
     */
    public HeartRateSensorEventListener(View heartRateView) {
        this.heartRateView = heartRateView;
    }
    
    /**
     * Clears {@link #measurements} to start testing heart rate again.
     */
    public void clearMeasurements() {
        measurements.clear();
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        float heartRate = event.values[0];
        
        if (heartRate > 0) {
            measurements.add(heartRate);
    
            float totalBpm = 0;
            for (float i : measurements) {
                totalBpm += i;
            }
    
            TextView heartRateAverage = heartRateView.findViewById(R.id.heart_rate_average);
            heartRateAverage.setText(heartRateView.getContext().getString(R.string.heart_rate_text, Math.round(totalBpm / measurements.size())));
            
            TextView heartRateCurrent = heartRateView.findViewById(R.id.heart_rate_current);
            heartRateCurrent.setText(heartRateView.getContext().getString(R.string.heart_rate_current_text, Math.round(heartRate)));
        }
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        TextView label = heartRateView.findViewById(R.id.heart_rate_label);
        
        if (accuracy > SensorManager.SENSOR_STATUS_UNRELIABLE) {
            label.setText(R.string.finger_on_sensor_message);
            
        } else {
            label.setText(R.string.place_finger_on_sensor_message);
    
            TextView heartRateAverage = heartRateView.findViewById(R.id.heart_rate_average);
            heartRateAverage.setText("");
    
            TextView heartRateCurrent = heartRateView.findViewById(R.id.heart_rate_current);
            heartRateCurrent.setText("");
            
            clearMeasurements();
        }
    }
}