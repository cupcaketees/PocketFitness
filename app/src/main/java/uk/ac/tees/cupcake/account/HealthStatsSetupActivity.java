package uk.ac.tees.cupcake.account;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import uk.ac.tees.cupcake.utils.views.ScaleValuePickerView;

import uk.ac.tees.cupcake.R;

/**
 *
 */
public class HealthStatsSetupActivity extends AppCompatActivity {
    
    private static final int MIN_HEIGHT = 100;
    
    private static final int MAX_HEIGHT = 220;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_stats_setup);
    
        addHeightPicker();
        addWeightPicker();
    }
    
    private void addHeightPicker() {
        TextView height = findViewById(R.id.health_stats_setup_height_label);
    
        ScaleValuePickerView heightPicker = findViewById(R.id.health_stats_height_picker);
        heightPicker.setMinValue(MIN_HEIGHT);
        heightPicker.setMaxValue(MAX_HEIGHT);
        heightPicker.setScaleStep(1);
        heightPicker.setScaleDivisionStep(5);
        heightPicker.setDerivedScaleWidth(4);
        heightPicker.setInitialValue(100);
    
        height.setText((int) heightPicker.getInitialValue() + "cm");
    
        heightPicker.setOnValueChangedListener(newValue -> height.setText((int) newValue + "cm"));
    }
    
    private static final int MIN_WEIGHT = 40;
    
    private static final int MAX_WEIGHT = 200;
    
    private void addWeightPicker() {
        TextView weight = findViewById(R.id.health_stats_setup_weight_label);
        
        ScaleValuePickerView weightPicker = findViewById(R.id.health_stats_weight_picker);
        weightPicker.setMinValue(MIN_WEIGHT);
        weightPicker.setMaxValue(MAX_WEIGHT);
        weightPicker.setScaleStep(1);
        weightPicker.setScaleDivisionStep(10);
        weightPicker.setDerivedScaleWidth(4);
        weightPicker.setInitialValue(MIN_WEIGHT);
        
        weight.setText((int) weightPicker.getInitialValue() + "kg");
        
        weightPicker.setOnValueChangedListener(newValue -> weight.setText((int) newValue + "kg"));
    }
    
}