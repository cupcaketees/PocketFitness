package uk.ac.tees.cupcake.home.health;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.sensors.SensorActivity;
import uk.ac.tees.cupcake.sensors.StepCounterSensorListener;

import static uk.ac.tees.cupcake.R.id.step_counter_view;

public class StepCounterActivity extends SensorActivity {
    
    @Override
    public void setupLayout() {
        setContentView(R.layout.activity_step_counter);
    
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BODY_SENSORS}, 0);
        }
    }
    
    @Override
    public int sensorType() {
        return Sensor.TYPE_STEP_COUNTER;
    }
    
    @Override
    public SensorEventListener eventListener() {
        View stepCounterView = findViewById(step_counter_view);
    
        return new StepCounterSensorListener(stepCounterView);
    }
}
