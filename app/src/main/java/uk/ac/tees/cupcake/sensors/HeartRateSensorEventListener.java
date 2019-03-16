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
import uk.ac.tees.cupcake.home.HomeActivity;
import uk.ac.tees.cupcake.home.health.HeartRateMeasurement;
import uk.ac.tees.cupcake.home.health.SaveHeartRateActivity;
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
    
    /**
     * The heart rate monitor view.
     */
    private final View heartRateView;
    
    private TextView bpm;
    private TextView label;
    private TextView heartRateText;
    private PulseView pulseView;
    
    /**
     * Constructs a new {@link HeartRateSensorEventListener}
     */
    public HeartRateSensorEventListener(View heartRateView) {
        this.heartRateView = heartRateView;
        this.bpm = heartRateView.findViewById(R.id.heart_rate_bpm);
        this.label = heartRateView.findViewById(R.id.heart_rate_label);
        this.heartRateText = heartRateView.findViewById(R.id.heart_rate_text);
        this.pulseView = heartRateView.findViewById(R.id.pv);
    }
    
    public void clearMeasurements() {
        measurements.clear();
    }
    
    public int getAverageMeasurement() {
        float totalBpm = 0;
        
        for (float i : measurements) {
            totalBpm += i;
        }
        
        return Math.round(totalBpm / measurements.size());
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        float heartRate = event.values[0];
        
        if (heartRate > 0) {
            measurements.add(heartRate);
        }
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (accuracy > SensorManager.SENSOR_STATUS_UNRELIABLE) {
            label.setText(R.string.finger_on_sensor_message);
            pulseView.startPulse();
            
            heartRateView.postDelayed(measurementCallback, 0);
        } else {
            heartRateView.removeCallbacks(measurementCallback);
            label.setText(R.string.place_finger_on_sensor_message);
            measurements.clear();
            
            ArcProgress progressBar = heartRateView.findViewById(R.id.arc_progress);
            progressBar.setProgress(0);
            
            heartRateText.setText(heartRateView.getContext().getString(R.string.heart_rate_text, 0));
            
            int colour = heartRateView.getResources().getColor(R.color.heart_rate_empty);
            bpm.setTextColor(colour);
            heartRateText.setTextColor(colour);
            
            pulseView.finishPulse();
        }
    }

    private void finish() {
        heartRateView.removeCallbacks(measurementCallback);
        pulseView.finishPulse();
        
        //todo: go to heart rate save screen
        HeartRateMeasurement measurement = new HeartRateMeasurement(System.currentTimeMillis(), getAverageMeasurement());
        
        Map<String, Serializable> extras = new HashMap<>();
        extras.put("heart_rate_measurement", measurement);
        IntentUtils.invokeViewWithExtras(heartRateView.getContext(), SaveHeartRateActivity.class, extras);
    }
    
    private final Runnable measurementCallback = new Runnable() {
        
        @Override
        public void run() {
            if (measurements.size() > SAMPLE_SIZE) {
                finish();
                return;
            }
    
            ArcProgress progressBar = heartRateView.findViewById(R.id.arc_progress);
            progressBar.setProgress(measurements.size() * 100 / SAMPLE_SIZE);
    
            int colour = heartRateView.getResources().getColor(R.color.heart_rate_measure);
            bpm.setTextColor(colour);
            heartRateText.setTextColor(colour);
    
            heartRateText.setText(heartRateView.getContext().getString(R.string.heart_rate_text, measurements.get(measurements.size() - 1).intValue()));
            heartRateView.postDelayed(this, 1000);
        }
    };
}