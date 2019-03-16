package uk.ac.tees.cupcake.home.health;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.sensors.HeartRateSensorEventListener;
import uk.ac.tees.cupcake.sensors.SensorAdapter;

/**
 * An activity for measuring heart rate.
 *
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public class HeartRateActivity extends AppCompatActivity {
    
    private HeartRateSensorEventListener eventListener;
    
    private SensorAdapter sensorAdapter = new SensorAdapter(this, Sensor.TYPE_HEART_RATE);
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        setContentView(R.layout.activity_heart_rate);
    
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BODY_SENSORS}, 0);
        }
    
        TextView heartRateAverage = findViewById(R.id.heart_rate_text);
        heartRateAverage.setText(getString(R.string.heart_rate_text, 0));
    
        eventListener = new HeartRateSensorEventListener(findViewById(R.id.heart_rate_view));
        
        sensorAdapter.onCreate();
    }
    
    @Override
    public void onPause() {
        super.onPause();
    
        eventListener.clearMeasurements();
        sensorAdapter.unregisterListener(Sensor.TYPE_HEART_RATE, eventListener);
    }
    
    @Override
    public void onResume() {
        super.onResume();
    
        eventListener.clearMeasurements();
        sensorAdapter.registerListener(SensorManager.SENSOR_DELAY_NORMAL, Sensor.TYPE_HEART_RATE, eventListener);
    }
    
}