package uk.ac.tees.cupcake.home.health;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
    public void setup() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
        View contentView = inflater.inflate(R.layout.activity_heart_rate, null, false);
        drawerLayout.addView(contentView, 0);
        
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BODY_SENSORS}, 0);
        }
        
        TextView heartRateAverage = findViewById(R.id.heart_rate_average);
        heartRateAverage.setText(getString(R.string.heart_rate_text, 0));
    }
    
    @Override
    public int sensorType() {
        return Sensor.TYPE_HEART_RATE;
    }
    
    @Override
    public SensorEventListener eventListener() {
        View heartRateView = findViewById(R.id.heart_rate_view);
        
        return new HeartRateSensorEventListener(heartRateView);
    }
    
}