package uk.ac.tees.cupcake.home.health;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.sensors.HeartRateSensorEventListener;

/**
 * An activity for measuring heart rate.
 *
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public class HeartRateActivity extends AppCompatActivity {
    
    /**
     * {@link SensorManager} allows usage of heart rate sensor.
     */
    private SensorManager sensorManager;
    
    /**
     * {@link Sensor} that we listen for events on.
     */
    private Sensor heartRateSensor;
    
    /**
     * Handles {@link SensorEvent}s from the {@link #heartRateSensor}.
     */
    private HeartRateSensorEventListener heartRateListener;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);
        
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.BODY_SENSORS }, 0);
        }
        
        if (setupSensor()) {
            View heartRateView = findViewById(R.id.heart_rate_view);
            
            heartRateListener = new HeartRateSensorEventListener(heartRateView);
    
            TextView heartRateAverage = heartRateView.findViewById(R.id.heart_rate_average);
            heartRateAverage.setText(heartRateView.getContext().getString(R.string.heart_rate_text, 0));
            
            registerListener();
        }
    }
    
    /**
     * Gets a heart rate sensor if one is available and assigns {@link #heartRateSensor} to it.
     *
     * @return {@code true} if sensor is successfully acquired.
     */
    private boolean setupSensor() {
        if ((sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE)) == null) {
            return false;
        }
        
        List<Sensor> availableSensors = sensorManager.getSensorList(Sensor.TYPE_HEART_RATE);
        
        if (availableSensors.size() > 0) {
            heartRateSensor = availableSensors.get(0);
        }
        
        return availableSensors.size() > 0;
    }
    
    /**
     * Registers the {@link #heartRateListener} with the {@link #heartRateSensor} and polls heart
     * rate once a second.
     */
    private void registerListener() {
        int delay = (int) TimeUnit.MICROSECONDS.convert(1, TimeUnit.SECONDS);
        
        sensorManager.registerListener(heartRateListener, heartRateSensor, delay);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        registerListener();
        heartRateListener.clearMeasurements();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
        sensorManager.unregisterListener(heartRateListener);
        heartRateListener.clearMeasurements();
    }
    
}