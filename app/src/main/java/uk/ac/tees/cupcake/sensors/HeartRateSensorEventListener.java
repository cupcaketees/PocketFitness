package uk.ac.tees.cupcake.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.TextView;

import com.gigamole.library.PulseView;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.home.health.heartrate.HeartRateActivity;
import uk.ac.tees.cupcake.home.health.heartrate.HeartRateMeasurement;
import uk.ac.tees.cupcake.home.health.heartrate.SaveHeartRateActivity;
import uk.ac.tees.cupcake.utils.IntentUtils;

/**
 * Listens for {@link SensorEvent}s from the heart rate sensor and then handles the events
 * appropriately.
 *
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public class HeartRateSensorEventListener implements SensorEventListener {
    
    /**
     * Amount of measurements to take.
     */
    private static final int SAMPLE_SIZE = 10;
    
    /**
     * Heart rate measurements taken in one reading.
     */
    private final ArrayList<Float> measurements = new ArrayList<>(SAMPLE_SIZE);
    
    private final View heartRateView;
    
    private boolean fingerOnSensor = true;
    
    private TextView bpm;
    private TextView label;
    private TextView heartRateText;
    private PulseView pulseView;
    
    /**
     * Constructs a new {@link HeartRateSensorEventListener}
     */
    public HeartRateSensorEventListener(HeartRateActivity heartRateActivity) {
        this.heartRateView = heartRateActivity.findViewById(R.id.heart_rate_view);
        this.bpm = heartRateActivity.findViewById(R.id.heart_rate_bpm);
        this.label = heartRateActivity.findViewById(R.id.heart_rate_label);
        this.heartRateText = heartRateActivity.findViewById(R.id.heart_rate_text);
        this.pulseView = heartRateActivity.findViewById(R.id.pv);
    }
    
    public void clearMeasurements() {
        measurements.clear();
    }
    
    /**
     * Calculates the average of the measurements taken.
     *
     * @return average heart rate of {@link #measurements}.
     */
    private int getAverageMeasurement() {
        float totalBpm = 0;
        
        for (float i : measurements) {
            totalBpm += i;
        }
        
        return Math.round(totalBpm / measurements.size());
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (measurements.size() >= SAMPLE_SIZE) {
            finish();
            return;
        }
        
        float heartRate = event.values[0];
        
        if (heartRate == 0) {
            if (fingerOnSensor = !fingerOnSensor) {
                label.setText(R.string.finger_on_sensor_message);
                pulseView.startPulse();
    
            } else {
                reset();
            }
        } else {
            measurements.add(heartRate);
        }
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (accuracy > SensorManager.SENSOR_STATUS_UNRELIABLE) {
            heartRateView.postDelayed(measurementCallback, 0);
        }
    }
    
    /**
     * Resets the view to a state where the finger is not on the sensor.
     */
    private void reset() {
        heartRateView.removeCallbacks(measurementCallback);
        pulseView.finishPulse();
        measurements.clear();
    
        ArcProgress progressBar = heartRateView.findViewById(R.id.arc_progress);
        progressBar.setProgress(0);
    
        heartRateText.setText(heartRateView.getContext().getString(R.string.heart_rate_text, 0));
        label.setText(R.string.place_finger_on_sensor_message);
        
        int colour = heartRateView.getResources().getColor(R.color.heart_rate_empty);
        bpm.setTextColor(colour);
        heartRateText.setTextColor(colour);
    }
    
    /**
     * Finishes the heart rate measurement process and moves to the measurement saving activity.
     */
    private void finish() {
        heartRateView.removeCallbacks(measurementCallback);
        pulseView.finishPulse();
        
        HeartRateMeasurement measurement = new HeartRateMeasurement(System.currentTimeMillis(), getAverageMeasurement(), "");
        
        Map<String, Serializable> extras = new HashMap<>();
        extras.put("heart_rate_measurement", measurement);
        
        IntentUtils.invokeViewWithExtras(heartRateView.getContext(), SaveHeartRateActivity.class, extras);
    }
    
    /**
     * Runnable to be invoked repeatedly where there are more measurements to be taken.
     */
    private final Runnable measurementCallback = new Runnable() {
        
        @Override
        public void run() {
            if (measurements.size() <= SAMPLE_SIZE) {
                ArcProgress progressBar = heartRateView.findViewById(R.id.arc_progress);
                progressBar.setProgress(measurements.size() * 100 / SAMPLE_SIZE);
    
                int colour = heartRateView.getResources().getColor(R.color.heart_rate_measure);
                bpm.setTextColor(colour);
                heartRateText.setTextColor(colour);
    
                heartRateText.setText(heartRateView.getContext().getString(R.string.heart_rate_text, measurements.get(measurements.size() - 1).intValue()));
                heartRateView.postDelayed(this, 1000);
            }
        }
    };
}