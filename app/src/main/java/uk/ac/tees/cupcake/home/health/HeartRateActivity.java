package uk.ac.tees.cupcake.home.health;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.sensors.HeartRateSensorEventListener;
import uk.ac.tees.cupcake.sensors.SensorActivity;

/**
 * An activity for measuring heart rate.
 *
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public class HeartRateActivity extends SensorActivity {
    
    @Override
    public void setupLayout() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BODY_SENSORS}, 0);
        }
        
        stub.setLayoutResource(R.layout.activity_heart_rate);
        stub.inflate();
        
        TextView heartRateAverage = findViewById(R.id.heart_rate_average);
        heartRateAverage.setText(getString(R.string.heart_rate_text, 0));
    }
    
    @Override
    public int sensorType() {
        return Sensor.TYPE_HEART_RATE;
    }
    
    @Override
    public SensorEventListener eventListener() {
        View view = findViewById(R.id.nav_content_layout);
        
        return new HeartRateSensorEventListener(view);
    }
    
    @Override
    public int delay() {
        return SensorManager.SENSOR_DELAY_FASTEST;
    }
    
}